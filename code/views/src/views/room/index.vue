<template>
  <div>
    <div>
      <Form :label-width="100" label-position="right">
        <Row class="code-row-bg">
          <Col span="6">
            <FormItem label="会议室名称: " prop="会议室名称: ">
              <Input v-model="roomName" maxlength="64" placeholder="会议室名称" style="width: 300px"/>
            </FormItem>
          </Col>
          <Col offset="2" span="6">
            <FormItem label="最小容量: " prop="最小容量: ">
              <Input v-model="minCapacity" class="number" placeholder="最小容量" style="width: 300px" type="number"/>
            </FormItem>
          </Col>
          <Col offset="2" span="6">
            <FormItem label="最大容量: " prop="最大容量: ">
              <Input v-model="maxCapacity" class="number" placeholder="最大容量" style="width: 300px" type="number"/>
            </FormItem>
          </Col>
        </Row>
      </Form>
    </div>
    <div style="text-align: right; margin-right: 200px">
      <Button style="margin-right: 10px"
              type="success"
              @click="getData()">确定
      </Button>
      <Button type="warning"
              @click="resetInfo()">重置
      </Button>
    </div>
    <div style="text-align: right; margin-right: 200px; margin-top: 20px">
      <Button type="primary">新增</Button>
    </div>
    <div style="margin-top: 30px">
      <Table :columns="roomColumns"
             :data="roomData" border/>
    </div>
    <div>
      <Page :current="pageNum"
            :page-size="pageSize"
            :total=this.total
            align="left"
            show-elevator
            show-sizer
            show-total
            @on-change="getPageNum"
            @on-page-size-change='getPageSize'/>
    </div>
  </div>
</template>

<script>
import {disableMeetRoom, enableMeetRoom, listMeetRoom} from "@/api/roomInfo.js"
import {time} from "@/utils/date.js"

export default {
  name: "index",
  data() {
    return {
      roomName: '',
      minCapacity: '',
      maxCapacity: '',
      pageNum: 1,
      pageSize: 10,
      total: 0,
      roomColumns: [
        {
          title: '序号',
          type: 'index',
          width: 80,
          align: 'center'
        },
        {
          title: '会议室名称',
          key: 'roomName',
          align: 'center',
          render: (h, params) => {
            return h('div', [
              h('Icon', {
                props: {
                  type: 'person'
                }
              }),
              h('strong', params.row.roomName)
            ]);
          }
        },
        {
          title: '容量',
          key: 'capacity',
          align: 'center'
        },
        {
          title: '状态',
          key: 'status',
          align: 'center',
          render: (h, params) => {
            let status = '';
            if (params.row.status === 0) {
              status = '禁用';
            } else if (params.row.status === 1) {
              status = '启用';
            }
            return h('div', [
              h('Icon', {
                props: {
                  type: 'person'
                }
              }),
              h('strong', status)
            ]);
          }
        },
        {
          title: '创建时间',
          key: 'createTime',
          align: 'center',
          render: (h, params) => {
            return h('div', [
              h('Icon', {
                props: {
                  type: 'person'
                }
              }),
              h('strong', time(params.row.createTime))
            ]);
          }
        },
        {
          title: '修改时间',
          key: 'updateTime',
          align: 'center',
          render: (h, params) => {
            return h('div', [
              h('Icon', {
                props: {
                  type: 'person'
                }
              }),
              h('strong', time(params.row.updateTime))
            ]);
          }
        },
        {
          title: '操作',
          key: 'action',
          width: 150,
          align: 'center',
          render: (h, params) => {
            return h('div', [
              h('Button', {
                props: {
                  type: 'primary',
                  size: 'small'
                },
                style: {
                  marginRight: '5px'
                },
                on: {
                  click: () => {
                    this.show(params.index)
                  }
                }
              }, '修改'),
              h('Button', {
                props: {
                  type: params.row.status === 0 ? 'info' : 'error',
                  size: 'small'
                },
                on: {
                  click: () => {
                    params.row.status === 0 ? this.enable(params.row.id) : this.disable(params.row.id)
                  }
                }
              }, params.row.status === 0 ? '启用' : '禁用')
            ]);
          }
        }
      ],
      roomData: []
    }
  },
  mounted() {
    this.getData();
  },
  methods: {
    show(index) {
      this.$Modal.info({
        title: 'Room Info',
        content: `会议室名称：${this.roomData[index].roomName}<br>容量：${this.roomData[index].capacity}
        <br>状态：${this.roomData[index].status}`
      })
    },
    async disable(id) {
      try {
        let {msg} = await disableMeetRoom(id);
        this.$Message.success(msg);
        await this.getData();
      } catch (e) {
        this.$Message.error("请求失败！");
      }
    },
    async enable(id) {
      try {
        let {msg} = await enableMeetRoom(id);
        this.$Message.success(msg);
        await this.getData();
      } catch (e) {
        this.$Message.error("请求失败！");
      }
    },
    getPageNum(value) {
      this.pageNum = value;
      this.getData();
    },
    getPageSize(value) {
      this.pageSize = value
      this.getData();
    },
    resetInfo() {
      this.roomName = '';
      this.minCapacity = '';
      this.maxCapacity = '';
    },
    async getData() {
      this.loading = true;
      let roomName = this.roomName.trim();
      try {
        let {
          msg,
          data
        } = await listMeetRoom("roomName=" + roomName + "&minCapacity=" + this.minCapacity + "&maxCapacity="
          + this.maxCapacity + "&pageNum=" + this.pageNum + "&pageSize=" + this.pageSize);
        this.$Message.success(msg);
        this.roomData = data.meetRoomInfo;
        this.total = data.total;
      } catch (e) {
        this.$Message.error("请求失败！");
      } finally {
        this.loading = false;
      }
    }
  }
}
</script>

<style scoped>
/deep/ .number input::-webkit-outer-spin-button,
/deep/ .number input::-webkit-inner-spin-button {
  -webkit-appearance: none;
}

/deep/ .number input[type="number"] {
  -moz-appearance: textfield;
}
</style>

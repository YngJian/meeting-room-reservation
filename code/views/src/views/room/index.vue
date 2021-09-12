<template>
  <div>
    <div>
      <Form :label-width="100">
        <Row class="code-row-bg" justify="center" type="flex">
          <Col span="8">
            <span>会议室名称: </span>
            <Input v-model="roomName" maxlength="64" placeholder="会议室名称" style="width: 200px"/>
          </Col>
          <Col span="8">
            <span>最小容量: </span>
            <Input v-model="minCapacity" class="number" placeholder="最小容量" style="width: 200px" type="number"/>
          </Col>
          <Col span="8">
            <span>最大容量: </span>
            <Input v-model="maxCapacity" class="number" placeholder="最大容量" style="width: 200px" type="number"/>
          </Col>
        </Row>
      </Form>
    </div>
    <div style="text-align: right; margin-right: 188px;margin-top: 20px">
      <Button style="margin-right: 10px"
              type="success"
              @click="getData()">确定
      </Button>
      <Button type="warning"
              @click="resetInfo()">重置
      </Button>
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
import {listMeetRoom} from "@/api/roomInfo.js"
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
          align: 'center'
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
              }, 'View'),
              h('Button', {
                props: {
                  type: 'error',
                  size: 'small'
                },
                on: {
                  click: () => {
                    this.remove(params.index)
                  }
                }
              }, 'Delete')
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
    remove(index) {
      this.roomData.splice(index, 1);
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

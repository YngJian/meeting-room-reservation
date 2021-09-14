<template>
  <div>
    <div>
      <Form :label-width="100" :model="formData" label-position="left">
        <FormItem label="会议室名称: ">
          <Input v-model="formData.roomName"></Input>
        </FormItem>
        <FormItem label="会议室容量: ">
          <Input v-model="formData.capacity"></Input>
        </FormItem>
      </Form>
    </div>
    <div>
      <Button type="primary">新增</Button>
      <Button type="warning">取消</Button>
    </div>
  </div>
</template>

<script>
export default {
  name: "add",
  data() {
    return {
      formData: {
        roomName: '',
        capacity: ''
      }
    }
  }
}
</script>

<style scoped>

</style>

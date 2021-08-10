import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../view/login'

Vue.use(VueRouter)

// 主页面MainPage下面的子页面
let pages = [
    'home',
    'prisonerManager',
    'playVolice',
    'textScroll',
    'test',
    'exchange',
    'uploadImg',
    'DualDatePicker',
    'iconButton',
    'base64ToFile'
].map(name => ({
    path: name,
    name: name,
    component: () =>
        import (`@/view/${name}`)
}))

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/login',
            name: 'login',
            component: Login
        },
    ]
})
export default router

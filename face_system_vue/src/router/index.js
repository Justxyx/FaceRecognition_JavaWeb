import Vue from 'vue'
import Router from 'vue-router'
import WebCamera from '../components/WebCamera'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'WebCamera',
      component: WebCamera
    }
  ]
})

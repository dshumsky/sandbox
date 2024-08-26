import Vue from '../libs/vue.js';
import {Navbar} from './c_navbar.js'
import {About} from './c_about.js'
import {Home} from './c_home.js'

Vue.use(VueRouter);

const router = new VueRouter({
    routes: [
        {
            path: '/',
            component: Home,
            name: "Home"
        },
        {
            path: '/about',
            component: About,
            name: "About Us Page"
        }
    ]
});

new Vue({
    el: '#app', // This should be the same as your <div id=""> from earlier.
    components: {
        'navbar': Navbar
    },
    router,
    template: `
    <div>
    <navbar></navbar>
    All content will appear below the horizontal line.
    <hr>
    <router-view></router-view>
    </div>
`
});

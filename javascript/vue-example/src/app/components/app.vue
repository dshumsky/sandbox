<template>
    <div id="app">
        <img src="../assets/images/logo.png"/>
        <p>Check <a href="https://github.com/xpepermint/vue-example/">vue-example</a> for more.</p>
        <h1>{{ message }}</h1>
    </div>
</template>

<script>
    module.exports = {
        data: function () {
            return {
                message: "Hello!"
            }
        }
    }
</script>

<style>
    @import '../assets/styles/scaffold.css';
</style>

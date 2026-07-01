<script setup>
import { RouterView } from 'vue-router'
</script>

<template>
  <RouterView />
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Segoe UI', -apple-system, BlinkMacSystemFont, Roboto, sans-serif;
  background: #1a1a1a;
  color: #ccc;
  line-height: 1.6;
}

a {
  text-decoration: none;
  color: inherit;
}

input {
  font-family: inherit;
}
</style>
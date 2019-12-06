# 快速创建项目

```bash
E:\demo>grails list-profiles
Resolving dependencies..
| Available Profiles
--------------------
* angular - A profile for creating Grails applications with Angular 5+
* rest-api - Profile for REST API applications
* base - The base profile extended by other profiles
* angularjs - A profile for creating applications using AngularJS
* plugin - Profile for plugins designed to work across all profiles
* profile - A profile for creating new Grails profiles
* react - A profile for creating Grails applications with a React frontend
* react-webpack - A profile for creating Grails applications with a React frontend using Webpack
* webpack - A profile for creating applications with node-based frontends using webpack 2
* web - Profile for Web applications
* rest-api-plugin - Profile for REST API plugins
* vue - A profile for creating Grails applications with a Vue.js frontend
* web-jboss7 - A Profile for Creating a JBoss 7.1 EAP Project
* web-plugin - Profile for Plugins designed for Web applications
E:\demo>grails create-app base --profile=vue
Resolving dependencies..
| Application created at E:\demo\base
| This profile provides a client/server multi-project build structure. The server Grails app is using the rest-api profile with CORS enabled. It can be started using 'grails run-app' or using the Gradle wrapper:

  ./gradlew server:bootRun

The Vue client app has been built via the Vue CLI (https://github.com/vuejs/vue-cli), using the webpack template. It can be started via 'npm start' (in which case you will need to run 'npm install' to install npm dependencies) or using the Gradle wrapper (which will install npm dependencies automatically if needed):

  ./gradlew client:start

For support, please use the Grails Community Slack (https://grails-slack.cfapps.io) or open an issue on Github: https://github.com/grails-profiles/vue/issues
```

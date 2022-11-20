# **Vue.js 源码目录设计**

```
vue
├── dist                        # 构建后的文件
├── examples                    # 例子
├── flow                        # flow 类型声明相关
├── packages
│   ├── vue-server-renderer
│   ├── vue-template-compiler
│   ├── weex-template-compiler
│   └── weex-vue-framework
├── README.md
├── scripts
├── src
│   ├── compiler                # 模板编译器
│   ├── core                    # 核心代码，与平台无关
│   │   ├─observe               # 变化侦听
│   │   ├─vdom                  # 虚拟DOM相关代码
│   │   ├─instance              # Vue 实例相关代码
│   │   ├─global-api            # 全局API相关代码
│   │   └─components            # 内置组件相关代码
│   ├── platforms               # 面向不同平台，不同环境的入口文件
│   ├── server                  # 服务器渲染相关代码
│   ├── sfc                     # 单文件组件
│   └── shared                  # 共享工具库
├── test                        # 测试代码
├── types                       # 类型声明文件
```

## **compiler**

compiler 目录包含 Vue.js 所有编译相关的代码。它包括把模板解析成 ast 语法树，ast 语法树优化，代码生成等功能。

编译的工作可以在构建时做（借助 webpack、vue-loader 等辅助插件）；也可以在运行时做，使用包含构建功能的 Vue.js。显然，编译是一项耗性能的工作，所以更推荐前者——离线编译。

## **core**

core 目录包含了 Vue.js 的核心代码，包括内置组件、全局 API 封装，Vue 实例化、观察者、虚拟 DOM、工具函数等等。

这里的代码可谓是 Vue.js 的灵魂，也是我们之后需要重点分析的地方。

## **platform**

Vue.js 是一个跨平台的 MVVM 框架，它可以跑在 web 上，也可以配合 weex 跑在 natvie 客户端上。platform 是 Vue.js 的入口，2 个目录代表 2 个主要入口，分别打包成运行在 web 上和 weex 上的 Vue.js。

我们会重点分析 web 入口打包后的 Vue.js，对于 weex 入口打包的 Vue.js，感兴趣的同学可以自行研究。

## **server**

Vue.js 2.0 支持了服务端渲染，所有服务端渲染相关的逻辑都在这个目录下。注意：这部分代码是跑在服务端的 Node.js，不要和跑在浏览器端的 Vue.js 混为一谈。

服务端渲染主要的工作是把组件渲染为服务器端的 HTML 字符串，将它们直接发送到浏览器，最后将静态标记"混合"为客户端上完全交互的应用程序。

## **sfc**

通常我们开发 Vue.js 都会借助 webpack 构建， 然后通过 .vue 单文件的编写组件。

这个目录下的代码逻辑会把 .vue 文件内容解析成一个 JavaScript 的对象。

## **shared**

Vue.js 会定义一些工具方法，这里定义的工具方法都是会被浏览器端的 Vue.js 和服务端的 Vue.js 所共享的。

# **Vue.js 源码构建**

Vue.js 源码是基于 [Rollup](https://github.com/rollup/rollup) 构建的，它的构建相关配置都在 scripts 目录下。

## **构建脚本**

通常一个基于 NPM 托管的项目都会有一个 package.json 文件，它是对项目的描述文件，它的内容实际上是一个标准的 JSON 对象。

我们通常会配置 script 字段作为 NPM 的执行脚本，Vue.js 源码构建的脚本如下：

```
{

 "script": {

  "build": "node scripts/build.js",

  "build:ssr": "npm run build -- web-runtime-cjs,web-server-renderer",

  "build:weex": "npm run build --weex"

 }

}
```

这里总共有 3 条命令，作用都是构建 Vue.js，后面 2 条是在第一条命令的基础上，添加一些环境参数。

当在命令行运行 npm run build 的时候，实际上就会执行 node scripts/build.js，接下来我们来看看它实际是怎么构建的。

## **构建过程**

我们对于构建过程分析是基于源码的，先打开构建的入口 JS 文件，在 scripts/build.js 中：

```
let builds = require('./config').getAllBuilds()

 

// filter builds via command line arg

if (process.argv[2]) {

 const filters = process.argv[2].split(',')

 builds = builds.filter(b => {

  return filters.some(f => b.output.file.indexOf(f) > -1 || b._name.indexOf(f) > -1)

 })

} else {

 // filter out weex builds by default

 builds = builds.filter(b => {

  return b.output.file.indexOf('weex') === -1

 })

}

 

build(builds)
```

这段代码逻辑非常简单，先从配置文件读取配置，再通过命令行参数对构建配置做过滤，这样就可以构建出不同用途的 Vue.js 了。接下来我们看一下配置文件，在 scripts/config.js 中：

```
const builds = {

 // Runtime only (CommonJS). Used by bundlers e.g. Webpack & Browserify

 'web-runtime-cjs': {

  entry: resolve('web/entry-runtime.js'),

  dest: resolve('dist/vue.runtime.common.js'),

  format: 'cjs',

  banner

 },

 // Runtime+compiler CommonJS build (CommonJS)

 'web-full-cjs': {

  entry: resolve('web/entry-runtime-with-compiler.js'),

  dest: resolve('dist/vue.common.js'),

  format: 'cjs',

  alias: { he: './entity-decoder' },

  banner

 },

 // Runtime only (ES Modules). Used by bundlers that support ES Modules,

 // e.g. Rollup & Webpack 2

 'web-runtime-esm': {

  entry: resolve('web/entry-runtime.js'),

  dest: resolve('dist/vue.runtime.esm.js'),

  format: 'es',

  banner

 },

 // Runtime+compiler CommonJS build (ES Modules)

 'web-full-esm': {

  entry: resolve('web/entry-runtime-with-compiler.js'),

  dest: resolve('dist/vue.esm.js'),

  format: 'es',

  alias: { he: './entity-decoder' },

  banner

 },

 // runtime-only build (Browser)

 'web-runtime-dev': {

  entry: resolve('web/entry-runtime.js'),

  dest: resolve('dist/vue.runtime.js'),

  format: 'umd',

  env: 'development',

  banner

 },

 // runtime-only production build (Browser)

 'web-runtime-prod': {

  entry: resolve('web/entry-runtime.js'),

  dest: resolve('dist/vue.runtime.min.js'),

  format: 'umd',

  env: 'production',

  banner

 },

 // Runtime+compiler development build (Browser)

 'web-full-dev': {

  entry: resolve('web/entry-runtime-with-compiler.js'),

  dest: resolve('dist/vue.js'),

  format: 'umd',

  env: 'development',

  alias: { he: './entity-decoder' },

  banner

 },

 // Runtime+compiler production build  (Browser)

 'web-full-prod': {

  entry: resolve('web/entry-runtime-with-compiler.js'),

  dest: resolve('dist/vue.min.js'),

  format: 'umd',

  env: 'production',

  alias: { he: './entity-decoder' },

  banner

 },

 // ...

}
```

这里列举了一些 Vue.js 构建的配置，关于还有一些服务端渲染 webpack 插件以及 weex 的打包配置就不列举了。

对于单个配置，它是遵循 Rollup 的构建规则的。其中 entry 属性表示构建的入口 JS 文件地址，dest 属性表示构建后的 JS 文件地址。format 属性表示构建的格式，cjs 表示构建出来的文件遵循 [CommonJS](http://wiki.commonjs.org/wiki/Modules/1.1) 规范，es 表示构建出来的文件遵循 [ES Module](http://exploringjs.com/es6/ch_modules.html) 规范。 umd 表示构建出来的文件遵循 [UMD](https://github.com/umdjs/umd) 规范。

以 web-runtime-cjs 配置为例，它的 entry 是
resolve('web/entry-runtime.js')，先来看一下 resolve 函数的定义。

源码目录：scripts/config.js

```
const aliases = require('./alias')

const resolve = p => {

 const base = p.split('/')[0]

 if (aliases[base]) {

  return path.resolve(aliases[base], p.slice(base.length + 1))

 } else {

  return path.resolve(__dirname, '../', p)

 }

}
```

这里的 resolve 函数实现非常简单，它先把 resolve 函数传入的参数 p 通过 / 做了分割成数组，然后取数组第一个元素设置为 base。在我们这个例子中，参数 p 是 web/entry-runtime.js，那么 base 则为 web。base 并不是实际的路径，它的真实路径借助了别名的配置，我们来看一下别名配置的代码，在 scripts/alias 中：

```
const path = require('path')

 

module.exports = {

 vue: path.resolve(__dirname, '../src/platforms/web/entry-runtime-with-compiler'),

 compiler: path.resolve(__dirname, '../src/compiler'),

 core: path.resolve(__dirname, '../src/core'),

 shared: path.resolve(__dirname, '../src/shared'),

 web: path.resolve(__dirname, '../src/platforms/web'),

 weex: path.resolve(__dirname, '../src/platforms/weex'),

 server: path.resolve(__dirname, '../src/server'),

 entries: path.resolve(__dirname, '../src/entries'),

 sfc: path.resolve(__dirname, '../src/sfc')

}
```

很显然，这里 web 对应的真实的路径是 path.resolve(__dirname, '../src/platforms/web')，这个路径就找到了 Vue.js 源码的 web 目录。然后 resolve 函数通过 path.resolve(aliases[base], p.slice(base.length + 1)) 找到了最终路径，它就是 Vue.js 源码 web 目录下的 entry-runtime.js。因此，web-runtime-cjs 配置对应的入口文件就找到了。

它经过 Rollup 的构建打包后，最终会在 dist 目录下生成 vue.runtime.common.js。

## **Runtime Only VS Runtime+Compiler**

通常我们利用 vue-cli 去初始化我们的 Vue.js 项目的时候会询问我们用 Runtime Only 版本的还是 Runtime+Compiler 版本。下面我们来对比这两个版本。

· Runtime Only

我们在使用 Runtime Only 版本的 Vue.js 的时候，通常需要借助如 webpack 的 vue-loader 工具把 .vue 文件编译成 JavaScript，因为是在编译阶段做的，所以它只包含运行时的 Vue.js 代码，因此代码体积也会更轻量。

· Runtime+Compiler

我们如果没有对代码做预编译，但又使用了 Vue 的 template 属性并传入一个字符串，则需要在客户端编译模板，如下所示：

```
// 需要编译器的版本

new Vue({

 template: '<div>{{ hi }}</div>'

})
 
// 这种情况不需要

new Vue({

 render (h) {

  return h('div', this.hi)

 }

})
```

因为在 Vue.js 2.0 中，最终渲染都是通过 render 函数，如果写 template 属性，则需要编译成 render 函数，那么这个编译过程会发生运行时，所以需要带有编译器的版本。

很显然，这个编译过程对性能会有一定损耗，所以通常我们更推荐使用 Runtime-Only 的 Vue.js。

# **从入口开始**

我们之前提到过 Vue.js 构建过程，在 web 应用下，我们来分析 Runtime + Compiler 构建出来的 Vue.js，它的入口是 src/platforms/web/entry-runtime-with-compiler.js：

```
/* @flow */

 

import config from 'core/config'

import { warn, cached } from 'core/util/index'

import { mark, measure } from 'core/util/perf'

 

import Vue from './runtime/index'

import { query } from './util/index'

import { compileToFunctions } from './compiler/index'

import { shouldDecodeNewlines, shouldDecodeNewlinesForHref } from './util/compat'

 

const idToTemplate = cached(id => {

 const el = query(id)

 return el && el.innerHTML

})

 

const mount = Vue.prototype.$mount

Vue.prototype.$mount = function (

 el?: string | Element,

 hydrating?: boolean

): Component {

 el = el && query(el)

 

 /* istanbul ignore if */

 if (el === document.body || el === document.documentElement) {

  process.env.NODE_ENV !== 'production' && warn(

   `Do not mount Vue to <html> or <body> - mount to normal elements instead.`

  )

  return this

 }

 

 const options = this.$options

 // resolve template/el and convert to render function

 if (!options.render) {

  let template = options.template

  if (template) {

   if (typeof template === 'string') {

​    if (template.charAt(0) === '#') {

​     template = idToTemplate(template)

​     /* istanbul ignore if */

​     if (process.env.NODE_ENV !== 'production' && !template) {

​      warn(

​       `Template element not found or is empty: ${options.template}`,

​       this

​      )

​     }

​    }

   } else if (template.nodeType) {

​    template = template.innerHTML

   } else {

​    if (process.env.NODE_ENV !== 'production') {

​     warn('invalid template option:' + template, this)

​    }

​    return this

   }

  } else if (el) {

   template = getOuterHTML(el)

  }

  if (template) {

   /* istanbul ignore if */

   if (process.env.NODE_ENV !== 'production' && config.performance && mark) {

​    mark('compile')

   }

 

   const { render, staticRenderFns } = compileToFunctions(template, {

​    shouldDecodeNewlines,

​    shouldDecodeNewlinesForHref,

​    delimiters: options.delimiters,

​    comments: options.comments

   }, this)

   options.render = render

   options.staticRenderFns = staticRenderFns

 

   /* istanbul ignore if */

   if (process.env.NODE_ENV !== 'production' && config.performance && mark) {

​    mark('compile end')

​    measure(`vue ${this._name} compile`, 'compile', 'compile end')

   }

  }

 }

 return mount.call(this, el, hydrating)

}

 

/**

 \* Get outerHTML of elements, taking care

 \* of SVG elements in IE as well.

 */

function getOuterHTML (el: Element): string {

 if (el.outerHTML) {

  return el.outerHTML

 } else {

  const container = document.createElement('div')

  container.appendChild(el.cloneNode(true))

  return container.innerHTML

 }

}

 

Vue.compile = compileToFunctions

 

export default Vue
```

那么，当我们的代码执行 import Vue from 'vue' 的时候，就是从这个入口执行代码来初始化 Vue，
那么 Vue 到底是什么，它是怎么初始化的，我们来一探究竟。

## **Vue 的入口**

在这个入口 JS 的上方我们可以找到 Vue 的来源：import Vue from './runtime/index'，我们先来看一下这块儿的实现，它定义在 src/platforms/web/runtime/index.js 中：

```
import Vue from 'core/index'

import config from 'core/config'

import { extend, noop } from 'shared/util'

import { mountComponent } from 'core/instance/lifecycle'

import { devtools, inBrowser, isChrome } from 'core/util/index'

 

import {

 query,

 mustUseProp,

 isReservedTag,

 isReservedAttr,

 getTagNamespace,

 isUnknownElement

} from 'web/util/index'

 

import { patch } from './patch'

import platformDirectives from './directives/index'

import platformComponents from './components/index'

 

// install platform specific utils

Vue.config.mustUseProp = mustUseProp

Vue.config.isReservedTag = isReservedTag

Vue.config.isReservedAttr = isReservedAttr

Vue.config.getTagNamespace = getTagNamespace

Vue.config.isUnknownElement = isUnknownElement

 

// install platform runtime directives & components

extend(Vue.options.directives, platformDirectives)

extend(Vue.options.components, platformComponents)

 

// install platform patch function

Vue.prototype.__patch__ = inBrowser ? patch : noop

 

// public mount method

Vue.prototype.$mount = function (

 el?: string | Element,

 hydrating?: boolean

): Component {

 el = el && inBrowser ? query(el) : undefined

 return mountComponent(this, el, hydrating)

}

 

// ...

 

export default Vue
```

这里关键的代码是 import Vue from 'core/index'，之后的逻辑都是对 Vue 这个对象做一些扩展，可以先不用看，我们来看一下真正初始化 Vue 的地方，在 src/core/index.js 中：

```
import Vue from './instance/index'

import { initGlobalAPI } from './global-api/index'

import { isServerRendering } from 'core/util/env'

import { FunctionalRenderContext } from 'core/vdom/create-functional-component'

 

initGlobalAPI(Vue)

 

Object.defineProperty(Vue.prototype, '$isServer', {

 get: isServerRendering

})

 

Object.defineProperty(Vue.prototype, '$ssrContext', {

 get () {

  /* istanbul ignore next */

  return this.$vnode && this.$vnode.ssrContext

 }

})

 

// expose FunctionalRenderContext for ssr runtime helper installation

Object.defineProperty(Vue, 'FunctionalRenderContext', {

 value: FunctionalRenderContext

})

 

Vue.version = '__VERSION__'

 

export default Vue
```

这里有 2 处关键的代码，import Vue from './instance/index' 和 initGlobalAPI(Vue)，初始化全局 Vue API（我们稍后介绍），我们先来看第一部分，在 src/core/instance/index.js 中：

### **Vue 的定义**

```
import { initMixin } from './init'

import { stateMixin } from './state'

import { renderMixin } from './render'

import { eventsMixin } from './events'

import { lifecycleMixin } from './lifecycle'

import { warn } from '../util/index'

 

function Vue (options) {

 if (process.env.NODE_ENV !== 'production' &&

  !(this instanceof Vue)

 ) {

  warn('Vue is a constructor and should be called with the `new` keyword')

 }

 this._init(options)

}

 

initMixin(Vue)

stateMixin(Vue)

eventsMixin(Vue)

lifecycleMixin(Vue)

renderMixin(Vue)

 

export default Vue
```

在这里，我们终于看到了 Vue 的庐山真面目，它实际上就是一个用 Function 实现的类，我们只能通过 new Vue 去实例化它。

有些同学看到这不禁想问，为何 Vue 不用 ES6 的 Class 去实现呢？我们往后看这里有很多 xxxMixin 的函数调用，并把 Vue 当参数传入，它们的功能都是给 Vue 的 prototype 上扩展一些方法（这里具体的细节会在之后的文章介绍，这里不展开），Vue 按功能把这些扩展分散到多个模块中去实现，而不是在一个模块里实现所有，这种方式是用 Class 难以实现的。这么做的好处是非常方便代码的维护和管理，这种编程技巧也非常值得我们去学习。

### initGlobalAPI

Vue.js 在整个初始化过程中，除了给它的原型 prototype 上扩展方法，还会给 Vue 这个对象本身扩展全局的静态方法，它的定义在 src/core/global-api/index.js 中：

```
export function initGlobalAPI (Vue: GlobalAPI) {

 // config

 const configDef = {}

 configDef.get = () => config

 if (process.env.NODE_ENV !== 'production') {

  configDef.set = () => {

   warn(

​    'Do not replace the Vue.config object, set individual fields instead.'

   )

  }

 }

 Object.defineProperty(Vue, 'config', configDef)

 

 // exposed util methods.

 // NOTE: these are not considered part of the public API - avoid relying on

 // them unless you are aware of the risk.

 Vue.util = {

  warn,

  extend,

  mergeOptions,

  defineReactive

 }

 

 Vue.set = set

 Vue.delete = del

 Vue.nextTick = nextTick

 

 Vue.options = Object.create(null)

 ASSET_TYPES.forEach(type => {

  Vue.options[type + 's'] = Object.create(null)

 })

 

 // this is used to identify the "base" constructor to extend all plain-object

 // components with in Weex's multi-instance scenarios.

 Vue.options._base = Vue

 

 extend(Vue.options.components, builtInComponents)

 

 initUse(Vue)

 initMixin(Vue)

 initExtend(Vue)

 initAssetRegisters(Vue)

}
```

这里就是在 Vue 上扩展的一些全局方法的定义，Vue 官网中关于全局 API 都可以在这里找到，这里不会介绍细节，会在之后的章节我们具体介绍到某个 API 的时候会详细介绍。有一点要注意的是，Vue.util 暴露的方法最好不要依赖，因为它可能经常会发生变化，是不稳定的。

# **Vue 实例挂载的实现**

Vue 中我们是通过 $mount 实例方法去挂载 vm 的，$mount 方法在多个文件中都有定义，如 src/platform/web/entry-runtime-with-compiler.js、src/platform/web/runtime/index.js、src/platform/weex/runtime/index.js。因为 $mount 这个方法的实现是和平台、构建方式都相关的。接下来我们重点分析带 compiler 版本的 $monut 实现，因为抛开 webpack 的 vue-loader，我们在纯前端浏览器环境分析 Vue 的工作原理，有助于我们对原理理解的深入。

compiler 版本的 $monut 实现非常有意思，先来看一下 src/platform/web/entry-runtime-with-compiler.js 文件中定义：

```
const mount = Vue.prototype.$mount

Vue.prototype.$mount = function (

 el?: string | Element,

 hydrating?: boolean

): Component {

 el = el && query(el)

 

 /* istanbul ignore if */

 if (el === document.body || el === document.documentElement) {

  process.env.NODE_ENV !== 'production' && warn(

   `Do not mount Vue to <html> or <body> - mount to normal elements instead.`

  )

  return this

 }

 

 const options = this.$options

 // resolve template/el and convert to render function

 if (!options.render) {

  let template = options.template

  if (template) {

   if (typeof template === 'string') {

​    if (template.charAt(0) === '#') {

​     template = idToTemplate(template)

​     /* istanbul ignore if */

​     if (process.env.NODE_ENV !== 'production' && !template) {

​      warn(

​       `Template element not found or is empty: ${options.template}`,

​       this

​      )

​     }

​    }

   } else if (template.nodeType) {

​    template = template.innerHTML

   } else {

​    if (process.env.NODE_ENV !== 'production') {

​     warn('invalid template option:' + template, this)

​    }

​    return this

   }

  } else if (el) {

   template = getOuterHTML(el)

  }

  if (template) {

   /* istanbul ignore if */

   if (process.env.NODE_ENV !== 'production' && config.performance && mark) {

​    mark('compile')

   }

 

   const { render, staticRenderFns } = compileToFunctions(template, {

​    shouldDecodeNewlines,

​    shouldDecodeNewlinesForHref,

​    delimiters: options.delimiters,

​    comments: options.comments

   }, this)

   options.render = render

   options.staticRenderFns = staticRenderFns

 

   /* istanbul ignore if */

   if (process.env.NODE_ENV !== 'production' && config.performance && mark) {

​    mark('compile end')

​    measure(`vue ${this._name} compile`, 'compile', 'compile end')

   }

  }

 }

 return mount.call(this, el, hydrating)

}
```

这段代码首先缓存了原型上的 $mount 方法，再重新定义该方法，我们先来分析这段代码。首先，它对 el 做了限制，Vue 不能挂载在 body、html 这样的根节点上。接下来的是很关键的逻辑 —— 如果没有定义 render 方法，则会把 el 或者 template 字符串转换成 render 方法。这里我们要牢记，在 Vue 2.0 版本中，所有 Vue 的组件的渲染最终都需要 render 方法，无论我们是用单文件 .vue 方式开发组件，还是写了 el 或者 template 属性，最终都会转换成 render 方法，那么这个过程是 Vue 的一个“在线编译”的过程，它是调用 compileToFunctions 方法实现的，编译过程我们之后会介绍。最后，调用原先原型上的 $mount 方法挂载。

原先原型上的 $mount 方法在 src/platform/web/runtime/index.js 中定义，之所以这么设计完全是为了复用，因为它是可以被 runtime only 版本的 Vue 直接使用的。

```
// public mount method

Vue.prototype.$mount = function (

 el?: string | Element,

 hydrating?: boolean

): Component {

 el = el && inBrowser ? query(el) : undefined

 return mountComponent(this, el, hydrating)

}
```

$mount 方法支持传入 2 个参数，第一个是 el，它表示挂载的元素，可以是字符串，也可以是 DOM 对象，如果是字符串在浏览器环境下会调用 query 方法转换成 DOM 对象的。第二个参数是和服务端渲染相关，在浏览器环境下我们不需要传第二个参数。

$mount 方法实际上会去调用 mountComponent 方法，这个方法定义在 src/core/instance/lifecycle.js 文件中：

```
export function mountComponent (

 vm: Component,

 el: ?Element,

 hydrating?: boolean

): Component {

 vm.$el = el

 if (!vm.$options.render) {

  vm.$options.render = createEmptyVNode

  if (process.env.NODE_ENV !== 'production') {

   /* istanbul ignore if */

   if ((vm.$options.template && vm.$options.template.charAt(0) !== '#') ||

​    vm.$options.el || el) {

​    warn(

​     'You are using the runtime-only build of Vue where the template ' +

​     'compiler is not available. Either pre-compile the templates into ' +

​     'render functions, or use the compiler-included build.',

​     vm

​    )

   } else {

​    warn(

​     'Failed to mount component: template or render function not defined.',

​     vm

​    )

   }

  }

 }

 callHook(vm, 'beforeMount')

 

 let updateComponent

 /* istanbul ignore if */

 if (process.env.NODE_ENV !== 'production' && config.performance && mark) {

  updateComponent = () => {

   const name = vm._name

   const id = vm._uid

   const startTag = `vue-perf-start:${id}`

   const endTag = `vue-perf-end:${id}`

 

   mark(startTag)

   const vnode = vm._render()

   mark(endTag)

   measure(`vue ${name} render`, startTag, endTag)

 

   mark(startTag)

   vm._update(vnode, hydrating)

   mark(endTag)

   measure(`vue ${name} patch`, startTag, endTag)

  }

 } else {

  updateComponent = () => {

   vm._update(vm._render(), hydrating)

  }

 }

 

 // we set this to vm._watcher inside the watcher's constructor

 // since the watcher's initial patch may call $forceUpdate (e.g. inside child

 // component's mounted hook), which relies on vm._watcher being already defined

 new Watcher(vm, updateComponent, noop, {

  before () {

   if (vm._isMounted) {

​    callHook(vm, 'beforeUpdate')

   }

  }

 }, true /* isRenderWatcher */)

 hydrating = false

 

 // manually mounted instance, call mounted on self

 // mounted is called for render-created child components in its inserted hook

 if (vm.$vnode == null) {

  vm._isMounted = true

  callHook(vm, 'mounted')

 }

 return vm

}
```

从上面的代码可以看到，mountComponent 核心就是先调用 vm._render 方法先生成虚拟 Node，再实例化一个渲染Watcher，在它的回调函数中会调用 updateComponent 方法，最终调用 vm._update 更新 DOM。

Watcher 在这里起到两个作用，一个是初始化的时候会执行回调函数，另一个是当 vm 实例中的监测的数据发生变化的时候执行回调函数，这块儿我们会在之后的章节中介绍。

函数最后判断为根节点的时候设置 vm._isMounted 为 true， 表示这个实例已经挂载了，同时执行 mounted 钩子函数。 这里注意 vm.$vnode 表示 Vue 实例的父虚拟 Node，所以它为 Null 则表示当前是根 Vue 的实例。

# **render**

Vue 的 _render 方法是实例的一个私有方法，它用来把实例渲染成一个虚拟 Node。它的定义在 src/core/instance/render.js 文件中：

```
Vue.prototype._render = function (): VNode {

 const vm: Component = this

 const { render, _parentVnode } = vm.$options

 

 // reset _rendered flag on slots for duplicate slot check

 if (process.env.NODE_ENV !== 'production') {

  for (const key in vm.$slots) {

   // $flow-disable-line

   vm.$slots[key]._rendered = false

  }

 }

 

 if (_parentVnode) {

  vm.$scopedSlots = _parentVnode.data.scopedSlots || emptyObject

 }

 

 // set parent vnode. this allows render functions to have access

 // to the data on the placeholder node.

 vm.$vnode = _parentVnode

 // render self

 let vnode

 try {

  vnode = render.call(vm._renderProxy, vm.$createElement)

 } catch (e) {

  handleError(e, vm, `render`)

  // return error render result,

  // or previous vnode to prevent render error causing blank component

  /* istanbul ignore else */

  if (process.env.NODE_ENV !== 'production') {

   if (vm.$options.renderError) {

​    try {

​     vnode = vm.$options.renderError.call(vm._renderProxy, vm.$createElement, e)

​    } catch (e) {

​     handleError(e, vm, `renderError`)

​     vnode = vm._vnode

​    }

   } else {

​    vnode = vm._vnode

   }

  } else {

   vnode = vm._vnode

  }

 }

 // return empty vnode in case the render function errored out

 if (!(vnode instanceof VNode)) {

  if (process.env.NODE_ENV !== 'production' && Array.isArray(vnode)) {

   warn(

​    'Multiple root nodes returned from render function. Render function ' +

​    'should return a single root node.',

​    vm

   )

  }

  vnode = createEmptyVNode()

 }

 // set parent

 vnode.parent = _parentVnode

 return vnode

}
```

这段代码最关键的是 render 方法的调用，我们在平时的开发工作中手写 render 方法的场景比较少，而写的比较多的是 template 模板，在之前的 mounted 方法的实现中，会把 template 编译成 render 方法，但这个编译过程是非常复杂的，我们不打算在这里展开讲，之后会专门花一个章节来分析 Vue 的编译过程。

在 Vue 的官方文档中介绍了 render 函数的第一个参数是 createElement，那么结合之前的例子：

```
<div id="app">

{{ message }}

</div>
```

相当于我们编写如下 render 函数：

```
render: function (createElement) {

 return createElement('div', {

   attrs: {

​    id: 'app'

   },

 }, this.message)

}
```

再回到 _render 函数中的 render 方法的调用：

vnode = render.call(vm._renderProxy, vm.$createElement)

可以看到，render 函数中的 createElement 方法就是 vm.$createElement 方法：

```
export function initRender (vm: Component) {

 // ...

 // bind the createElement fn to this instance

 // so that we get proper render context inside it.

 // args order: tag, data, children, normalizationType, alwaysNormalize

 // internal version is used by render functions compiled from templates

 vm._c = (a, b, c, d) => createElement(vm, a, b, c, d, false)

 // normalization is always applied for the public version, used in

 // user-written render functions.

 vm.$createElement = (a, b, c, d) => createElement(vm, a, b, c, d, true)

}
```

实际上，vm.$createElement 方法定义是在执行 initRender 方法的时候，可以看到除了 vm.$createElement 方法，还有一个 vm._c 方法，它是被模板编译成的 render 函数使用，而 vm.$createElement 是用户手写 render 方法使用的， 这俩个方法支持的参数相同，并且内部都调用了 createElement 方法。

## **总结**

vm._render 最终是通过执行 createElement 方法并返回的是 vnode，它是一个虚拟 Node。Vue 2.0 相比 Vue 1.0 最大的升级就是利用了 Virtual DOM。因此在分析 createElement 的实现前，我们先了解一下 Virtual DOM 的概念。

# **Virtual DOM**

Virtual DOM 这个概念相信大部分人都不会陌生，它产生的前提是浏览器中的 DOM 是很“昂贵"的，为了更直观的感受，我们可以简单的把一个简单的 div 元素的属性都打印出来

可以看到，真正的 DOM 元素是非常庞大的，因为浏览器的标准就把 DOM 设计的非常复杂。当我们频繁的去做 DOM 更新，会产生一定的性能问题。

而 Virtual DOM 就是用一个原生的 JS 对象去描述一个 DOM 节点，所以它比创建一个 DOM 的代价要小很多。在 Vue.js 中，Virtual DOM 是用 VNode 这么一个 Class 去描述，它是定义在 src/core/vdom/vnode.js 中的。

```
export default class VNode {

 tag: string | void;

 data: VNodeData | void;

 children: ?Array<VNode>;

 text: string | void;

 elm: Node | void;

 ns: string | void;

 context: Component | void; // rendered in this component's scope

 key: string | number | void;

 componentOptions: VNodeComponentOptions | void;

 componentInstance: Component | void; // component instance

 parent: VNode | void; // component placeholder node

 

 // strictly internal

 raw: boolean; // contains raw HTML? (server only)

 isStatic: boolean; // hoisted static node

 isRootInsert: boolean; // necessary for enter transition check

 isComment: boolean; // empty comment placeholder?

 isCloned: boolean; // is a cloned node?

 isOnce: boolean; // is a v-once node?

 asyncFactory: Function | void; // async component factory function

 asyncMeta: Object | void;

 isAsyncPlaceholder: boolean;

 ssrContext: Object | void;

 fnContext: Component | void; // real context vm for functional nodes

 fnOptions: ?ComponentOptions; // for SSR caching

 fnScopeId: ?string; // functional scope id support

 

 constructor (

  tag?: string,

  data?: VNodeData,

  children?: ?Array<VNode>,

  text?: string,

  elm?: Node,

  context?: Component,

  componentOptions?: VNodeComponentOptions,

  asyncFactory?: Function

 ) {

  this.tag = tag

  this.data = data

  this.children = children

  this.text = text

  this.elm = elm

  this.ns = undefined

  this.context = context

  this.fnContext = undefined

  this.fnOptions = undefined

  this.fnScopeId = undefined

  this.key = data && data.key

  this.componentOptions = componentOptions

  this.componentInstance = undefined

  this.parent = undefined

  this.raw = false

  this.isStatic = false

  this.isRootInsert = true

  this.isComment = false

  this.isCloned = false

  this.isOnce = false

  this.asyncFactory = asyncFactory

  this.asyncMeta = undefined

  this.isAsyncPlaceholder = false

 }

 

 // DEPRECATED: alias for componentInstance for backwards compat.

 /* istanbul ignore next */

 get child (): Component | void {

  return this.componentInstance

 }

}
```

可以看到 Vue.js 中的 Virtual DOM 的定义还是略微复杂一些的，因为它这里包含了很多 Vue.js 的特性。这里千万不要被这些茫茫多的属性吓到，实际上 Vue.js 中 Virtual DOM 是借鉴了一个开源库 [snabbdom](https://github.com/snabbdom/snabbdom) 的实现，然后加入了一些 Vue.js 特色的东西。我建议大家如果想深入了解 Vue.js 的 Virtual DOM 前不妨先阅读这个库的源码，因为它更加简单和纯粹。

## **总结**

其实 VNode 是对真实 DOM 的一种抽象描述，它的核心定义无非就几个关键属性，标签名、数据、子节点、键值等，其它属性都是都是用来扩展 VNode 的灵活性以及实现一些特殊 feature 的。由于 VNode 只是用来映射到真实 DOM 的渲染，不需要包含操作 DOM 的方法，因此它是非常轻量和简单的。

Virtual DOM 除了它的数据结构的定义，映射到真实的 DOM 实际上要经历 VNode 的 create、diff、patch 等过程。那么在 Vue.js 中，VNode 的 create 是通过之前提到的 createElement 方法创建的，我们接下来分析这部分的实现。

# **createElement**

Vue.js 利用 createElement 方法创建 VNode，它定义在 src/core/vdom/create-elemenet.js 中：

```
// wrapper function for providing a more flexible interface

// without getting yelled at by flow

export function createElement (

 context: Component,

 tag: any,

 data: any,

 children: any,

 normalizationType: any,

 alwaysNormalize: boolean

): VNode | Array<VNode> {

 if (Array.isArray(data) || isPrimitive(data)) {

  normalizationType = children

  children = data

  data = undefined

 }

 if (isTrue(alwaysNormalize)) {

  normalizationType = ALWAYS_NORMALIZE

 }

 return _createElement(context, tag, data, children, normalizationType)

}
```

createElement 方法实际上是对 _createElement 方法的封装，它允许传入的参数更加灵活，在处理这些参数后，调用真正创建 VNode 的函数 _createElement：

```
export function _createElement (

 context: Component,

 tag?: string | Class<Component> | Function | Object,

 data?: VNodeData,

 children?: any,

 normalizationType?: number

): VNode | Array<VNode> {

 if (isDef(data) && isDef((data: any).__ob__)) {

  process.env.NODE_ENV !== 'production' && warn(

   `Avoid using observed data object as vnode data: ${JSON.stringify(data)}\n` +

   'Always create fresh vnode data objects in each render!',

   context

  )

  return createEmptyVNode()

 }

 // object syntax in v-bind

 if (isDef(data) && isDef(data.is)) {

  tag = data.is

 }

 if (!tag) {

  // in case of component :is set to falsy value

  return createEmptyVNode()

 }

 // warn against non-primitive key

 if (process.env.NODE_ENV !== 'production' &&

  isDef(data) && isDef(data.key) && !isPrimitive(data.key)

 ) {

  if (!__WEEX__ || !('@binding' in data.key)) {

   warn(

​    'Avoid using non-primitive value as key, ' +

​    'use string/number value instead.',

​    context

   )

  }

 }

 // support single function children as default scoped slot

 if (Array.isArray(children) &&

  typeof children[0] === 'function'

 ) {

  data = data || {}

  data.scopedSlots = { default: children[0] }

  children.length = 0

 }

 if (normalizationType === ALWAYS_NORMALIZE) {

  children = normalizeChildren(children)

 } else if (normalizationType === SIMPLE_NORMALIZE) {

  children = simpleNormalizeChildren(children)

 }

 let vnode, ns

 if (typeof tag === 'string') {

  let Ctor

  ns = (context.$vnode && context.$vnode.ns) || config.getTagNamespace(tag)

  if (config.isReservedTag(tag)) {

   // platform built-in elements

   vnode = new VNode(

​    config.parsePlatformTagName(tag), data, children,

​    undefined, undefined, context

   )

  } else if (isDef(Ctor = resolveAsset(context.$options, 'components', tag))) {

   // component

   vnode = createComponent(Ctor, data, context, children, tag)

  } else {

   // unknown or unlisted namespaced elements

   // check at runtime because it may get assigned a namespace when its

   // parent normalizes children

   vnode = new VNode(

​    tag, data, children,

​    undefined, undefined, context

   )

  }

 } else {

  // direct component options / constructor

  vnode = createComponent(tag, data, context, children)

 }

 if (Array.isArray(vnode)) {

  return vnode

 } else if (isDef(vnode)) {

  if (isDef(ns)) applyNS(vnode, ns)

  if (isDef(data)) registerDeepBindings(data)

  return vnode

 } else {

  return createEmptyVNode()

 }

}
```

_createElement 方法有 5 个参数，context 表示 VNode 的上下文环境，它是 Component 类型；tag 表示标签，它可以是一个字符串，也可以是一个 Component；data 表示 VNode 的数据，它是一个 VNodeData 类型，可以在 flow/vnode.js 中找到它的定义，这里先不展开说；children 表示当前 VNode 的子节点，它是任意类型的，它接下来需要被规范为标准的 VNode 数组；normalizationType 表示子节点规范的类型，类型不同规范的方法也就不一样，它主要是参考 render 函数是编译生成的还是用户手写的。

createElement 函数的流程略微有点多，我们接下来主要分析 2 个重点的流程 —— children 的规范化以及 VNode 的创建。

## **children 的规范化**

由于 Virtual DOM 实际上是一个树状结构，每一个 VNode 可能会有若干个子节点，这些子节点应该也是 VNode 的类型。_createElement 接收的第 4 个参数 children 是任意类型的，因此我们需要把它们规范成 VNode 类型。

这里根据 normalizationType 的不同，调用了 normalizeChildren(children) 和 simpleNormalizeChildren(children) 方法，它们的定义都在 src/core/vdom/helpers/normalzie-children.js 中：

```
// The template compiler attempts to minimize the need for normalization by

// statically analyzing the template at compile time.

//

// For plain HTML markup, normalization can be completely skipped because the

// generated render function is guaranteed to return Array<VNode>. There are

// two cases where extra normalization is needed:

 

// 1. When the children contains components - because a functional component

// may return an Array instead of a single root. In this case, just a simple

// normalization is needed - if any child is an Array, we flatten the whole

// thing with Array.prototype.concat. It is guaranteed to be only 1-level deep

// because functional components already normalize their own children.

export function simpleNormalizeChildren (children: any) {

 for (let i = 0; i < children.length; i++) {

  if (Array.isArray(children[i])) {

   return Array.prototype.concat.apply([], children)

  }

 }

 return children

}

 

// 2. When the children contains constructs that always generated nested Arrays,

// e.g. <template>, <slot>, v-for, or when the children is provided by user

// with hand-written render functions / JSX. In such cases a full normalization

// is needed to cater to all possible types of children values.

export function normalizeChildren (children: any): ?Array<VNode> {

 return isPrimitive(children)

  ? [createTextVNode(children)]

  : Array.isArray(children)

   ? normalizeArrayChildren(children)

   : undefined

}
```

simpleNormalizeChildren 方法调用场景是 render 函数当函数是编译生成的。理论上编译生成的 children 都已经是 VNode 类型的，但这里有一个例外，就是 functional component 函数式组件返回的是一个数组而不是一个根节点，所以会通过 Array.prototype.concat 方法把整个 children 数组打平，让它的深度只有一层。

normalizeChildren 方法的调用场景有 2 种，一个场景是 render 函数是用户手写的，当 children 只有一个节点的时候，Vue.js 从接口层面允许用户把 children 写成基础类型用来创建单个简单的文本节点，这种情况会调用 createTextVNode 创建一个文本节点的 VNode；另一个场景是当编译 slot、v-for 的时候会产生嵌套数组的情况，会调用 normalizeArrayChildren 方法，接下来看一下它的实现：

```
function normalizeArrayChildren (children: any, nestedIndex?: string): Array<VNode> {

 const res = []

 let i, c, lastIndex, last

 for (i = 0; i < children.length; i++) {

  c = children[i]

  if (isUndef(c) || typeof c === 'boolean') continue

  lastIndex = res.length - 1

  last = res[lastIndex]

  //  nested

  if (Array.isArray(c)) {

   if (c.length > 0) {

​    c = normalizeArrayChildren(c, `${nestedIndex || ''}_${i}`)

​    // merge adjacent text nodes

​    if (isTextNode(c[0]) && isTextNode(last)) {

​     res[lastIndex] = createTextVNode(last.text + (c[0]: any).text)

​     c.shift()

​    }

​    res.push.apply(res, c)

   }

  } else if (isPrimitive(c)) {

   if (isTextNode(last)) {

​    // merge adjacent text nodes

​    // this is necessary for SSR hydration because text nodes are

​    // essentially merged when rendered to HTML strings

​    res[lastIndex] = createTextVNode(last.text + c)

   } else if (c !== '') {

​    // convert primitive to vnode

​    res.push(createTextVNode(c))

   }

  } else {

   if (isTextNode(c) && isTextNode(last)) {

​    // merge adjacent text nodes

​    res[lastIndex] = createTextVNode(last.text + c.text)

   } else {

​    // default key for nested array children (likely generated by v-for)

​    if (isTrue(children._isVList) &&

​     isDef(c.tag) &&

​     isUndef(c.key) &&

​     isDef(nestedIndex)) {

​     c.key = `__vlist${nestedIndex}_${i}__`

​    }

​    res.push(c)

   }

  }

 }

 return res

}
```

normalizeArrayChildren 接收 2 个参数，children 表示要规范的子节点，nestedIndex 表示嵌套的索引，因为单个 child 可能是一个数组类型。 normalizeArrayChildren 主要的逻辑就是遍历 children，获得单个节点 c，然后对 c 的类型判断，如果是一个数组类型，则递归调用 normalizeArrayChildren; 如果是基础类型，则通过 createTextVNode 方法转换成 VNode 类型；否则就已经是 VNode 类型了，如果 children 是一个列表并且列表还存在嵌套的情况，则根据 nestedIndex 去更新它的 key。这里需要注意一点，在遍历的过程中，对这 3 种情况都做了如下处理：如果存在两个连续的 text节点，会把它们合并成一个 text 节点。

经过对 children 的规范化，children 变成了一个类型为 VNode 的 Array。

## **VNode 的创建**

回到 createElement 函数，规范化 children 后，接下来会去创建一个 VNode 的实例：

```
let vnode, ns

if (typeof tag === 'string') {

 let Ctor

 ns = (context.$vnode && context.$vnode.ns) || config.getTagNamespace(tag)

 if (config.isReservedTag(tag)) {

  // platform built-in elements

  vnode = new VNode(

   config.parsePlatformTagName(tag), data, children,

   undefined, undefined, context

  )

 } else if (isDef(Ctor = resolveAsset(context.$options, 'components', tag))) {

  // component

  vnode = createComponent(Ctor, data, context, children, tag)

 } else {

  // unknown or unlisted namespaced elements

  // check at runtime because it may get assigned a namespace when its

  // parent normalizes children

  vnode = new VNode(

   tag, data, children,

   undefined, undefined, context

  )

 }

} else {

 // direct component options / constructor

 vnode = createComponent(tag, data, context, children)

}
```

这里先对 tag 做判断，如果是 string 类型，则接着判断如果是内置的一些节点，则直接创建一个普通 VNode，如果是为已注册的组件名，则通过 createComponent 创建一个组件类型的 VNode，否则创建一个未知的标签的 VNode。 如果是 tag 一个 Component 类型，则直接调用 createComponent 创建一个组件类型的 VNode 节点。对于 createComponent 创建组件类型的 VNode 的过程，我们之后会去介绍，本质上它还是返回了一个 VNode。

## **总结**

那么至此，我们大致了解了 createElement 创建 VNode 的过程，每个 VNode 有 children，children 每个元素也是一个 VNode，这样就形成了一个 VNode Tree，它很好的描述了我们的 DOM Tree。

回到 mountComponent 函数的过程，我们已经知道 vm._render 是如何创建了一个 VNode，接下来就是要把这个 VNode 渲染成一个真实的 DOM 并渲染出来，这个过程是通过 vm._update 完成的，接下来分析一下这个过程。 

# **update**

Vue 的 _update 是实例的一个私有方法，它被调用的时机有 2 个，一个是首次渲染，一个是数据更新的时候；由于我们这一章节只分析首次渲染部分，数据更新部分会在之后分析响应式原理的时候涉及。_update 方法的作用是把 VNode 渲染成真实的 DOM，它的定义在 src/core/instance/lifecycle.js 中：

```
Vue.prototype._update = function (vnode: VNode, hydrating?: boolean) {

 const vm: Component = this

 const prevEl = vm.$el

 const prevVnode = vm._vnode

 const prevActiveInstance = activeInstance

 activeInstance = vm

 vm._vnode = vnode

 // Vue.prototype.__patch__ is injected in entry points

 // based on the rendering backend used.

 if (!prevVnode) {

  // initial render

  vm.$el = vm.__patch__(vm.$el, vnode, hydrating, false /* removeOnly */)

 } else {

  // updates

  vm.$el = vm.__patch__(prevVnode, vnode)

 }

 activeInstance = prevActiveInstance

 // update __vue__ reference

 if (prevEl) {

  prevEl.__vue__ = null

 }

 if (vm.$el) {

  vm.$el.__vue__ = vm

 }

 // if parent is an HOC, update its $el as well

 if (vm.$vnode && vm.$parent && vm.$vnode === vm.$parent._vnode) {

  vm.$parent.$el = vm.$el

 }

 // updated hook is called by the scheduler to ensure that children are

 // updated in a parent's updated hook.

}
```

_update 的核心就是调用 vm.__patch__ 方法，这个方法实际上在不同的平台，比如 web 和 weex 上的定义是不一样的，因此在 web 平台中它的定义在 src/platforms/web/runtime/index.js 中：

Vue.prototype.__patch__ = inBrowser ? patch : noop

可以看到，甚至在 web 平台上，是否是服务端渲染也会对这个方法产生影响。因为在服务端渲染中，没有真实的浏览器 DOM 环境，所以不需要把 VNode 最终转换成 DOM，因此是一个空函数，而在浏览器端渲染中，它指向了 patch 方法，它的定义在 src/platforms/web/runtime/patch.js中：

```
import * as nodeOps from 'web/runtime/node-ops'

import { createPatchFunction } from 'core/vdom/patch'

import baseModules from 'core/vdom/modules/index'

import platformModules from 'web/runtime/modules/index'

 

// the directive module should be applied last, after all

// built-in modules have been applied.

const modules = platformModules.concat(baseModules)

 

export const patch: Function = createPatchFunction({ nodeOps, modules })
```

该方法的定义是调用 createPatchFunction 方法的返回值，这里传入了一个对象，包含 nodeOps 参数和 modules 参数。其中，nodeOps 封装了一系列 DOM 操作的方法，modules 定义了一些模块的钩子函数的实现，我们这里先不详细介绍，来看一下 createPatchFunction 的实现，它定义在 src/core/vdom/patch.js中：

```
const hooks = ['create', 'activate', 'update', 'remove', 'destroy']

 

export function createPatchFunction (backend) {

 let i, j

 const cbs = {}

 

 const { modules, nodeOps } = backend

 

 for (i = 0; i < hooks.length; ++i) {

  cbs[hooks[i]] = []

  for (j = 0; j < modules.length; ++j) {

   if (isDef(modules[j][hooks[i]])) {

​    cbs[hooks[i]].push(modules[j][hooks[i]])

   }

  }

 }

 

 // ...

 

 return function patch (oldVnode, vnode, hydrating, removeOnly) {

  if (isUndef(vnode)) {

   if (isDef(oldVnode)) invokeDestroyHook(oldVnode)

   return

  }

 

  let isInitialPatch = false

  const insertedVnodeQueue = []

 

  if (isUndef(oldVnode)) {

   // empty mount (likely as component), create new root element

   isInitialPatch = true

   createElm(vnode, insertedVnodeQueue)

  } else {

   const isRealElement = isDef(oldVnode.nodeType)

   if (!isRealElement && sameVnode(oldVnode, vnode)) {

​    // patch existing root node

​    patchVnode(oldVnode, vnode, insertedVnodeQueue, removeOnly)

   } else {

​    if (isRealElement) {

​     // mounting to a real element

​     // check if this is server-rendered content and if we can perform

​     // a successful hydration.

​     if (oldVnode.nodeType === 1 && oldVnode.hasAttribute(SSR_ATTR)) {

​      oldVnode.removeAttribute(SSR_ATTR)

​      hydrating = true

​     }

​     if (isTrue(hydrating)) {

​      if (hydrate(oldVnode, vnode, insertedVnodeQueue)) {

​       invokeInsertHook(vnode, insertedVnodeQueue, true)

​       return oldVnode

​      } else if (process.env.NODE_ENV !== 'production') {

​       warn(

​        'The client-side rendered virtual DOM tree is not matching ' +

​        'server-rendered content. This is likely caused by incorrect ' +

​        'HTML markup, for example nesting block-level elements inside ' +

​        '<p>, or missing <tbody>. Bailing hydration and performing ' +

​        'full client-side render.'

​       )

​      }

​     }

​     // either not server-rendered, or hydration failed.

​     // create an empty node and replace it

​     oldVnode = emptyNodeAt(oldVnode)

​    }

 

​    // replacing existing element

​    const oldElm = oldVnode.elm

​    const parentElm = nodeOps.parentNode(oldElm)

 

​    // create new node

​    createElm(

​     vnode,

​     insertedVnodeQueue,

​     // extremely rare edge case: do not insert if old element is in a

​     // leaving transition. Only happens when combining transition +

​     // keep-alive + HOCs. (#4590)

​     oldElm._leaveCb ? null : parentElm,

​     nodeOps.nextSibling(oldElm)

​    )

 

​    // update parent placeholder node element, recursively

​    if (isDef(vnode.parent)) {

​     let ancestor = vnode.parent

​     const patchable = isPatchable(vnode)

​     while (ancestor) {

​      for (let i = 0; i < cbs.destroy.length; ++i) {

​       cbs.destroy[i](ancestor)

​      }

​      ancestor.elm = vnode.elm

​      if (patchable) {

​       for (let i = 0; i < cbs.create.length; ++i) {

​        cbs.create[i](emptyNode, ancestor)

​       }

​       // #6513

​       // invoke insert hooks that may have been merged by create hooks.

​       // e.g. for directives that uses the "inserted" hook.

​       const insert = ancestor.data.hook.insert

​       if (insert.merged) {

​        // start at index 1 to avoid re-invoking component mounted hook

​        for (let i = 1; i < insert.fns.length; i++) {

​         insert.fns[i]()

​        }

​       }

​      } else {

​       registerRef(ancestor)

​      }

​      ancestor = ancestor.parent

​     }

​    }

 

​    // destroy old node

​    if (isDef(parentElm)) {

​     removeVnodes(parentElm, [oldVnode], 0, 0)

​    } else if (isDef(oldVnode.tag)) {

​     invokeDestroyHook(oldVnode)

​    }

   }

  }

 

  invokeInsertHook(vnode, insertedVnodeQueue, isInitialPatch)

  return vnode.elm

 }

}
```

createPatchFunction 内部定义了一系列的辅助方法，最终返回了一个 patch 方法，这个方法就赋值给了 vm._update 函数里调用的 vm.__patch__。

在介绍 patch 的方法实现之前，我们可以思考一下为何 Vue.js 源码绕了这么一大圈，把相关代码分散到各个目录。因为前面介绍过，patch 是平台相关的，在 Web 和 Weex 环境，它们把虚拟 DOM 映射到 “平台 DOM” 的方法是不同的，并且对 “DOM” 包括的属性模块创建和更新也不尽相同。因此每个平台都有各自的 nodeOps 和 modules，它们的代码需要托管在 src/platforms 这个大目录下。

而不同平台的 patch 的主要逻辑部分是相同的，所以这部分公共的部分托管在 core 这个大目录下。差异化部分只需要通过参数来区别，这里用到了一个函数柯里化的技巧，通过 createPatchFunction 把差异化参数提前固化，这样不用每次调用 patch 的时候都传递 nodeOps 和 modules 了，这种编程技巧也非常值得学习。

在这里，nodeOps 表示对 “平台 DOM” 的一些操作方法，modules 表示平台的一些模块，它们会在整个 patch 过程的不同阶段执行相应的钩子函数。这些代码的具体实现会在之后的章节介绍。

回到 patch 方法本身，它接收 4个参数，oldVnode 表示旧的 VNode 节点，它也可以不存在或者是一个 DOM 对象；vnode 表示执行 _render 后返回的 VNode 的节点；hydrating 表示是否是服务端渲染；removeOnly 是给 transition-group 用的，之后会介绍。

patch 的逻辑看上去相对复杂，因为它有着非常多的分支逻辑，为了方便理解，我们并不会在这里介绍所有的逻辑，仅会针对我们之前的例子分析它的执行逻辑。之后我们对其它场景做源码分析的时候会再次回顾 patch 方法。

先来回顾我们的例子：

```
var app = new Vue({

 el: '#app',

 render: function (createElement) {

  return createElement('div', {

   attrs: {

​    id: 'app'

   },

  }, this.message)

 },

 data: {

  message: 'Hello Vue!'

 }

})
```

然后我们在 vm._update 的方法里是这么调用 patch 方法的：

// initial render

vm.$el = vm.__patch__(vm.$el, vnode, hydrating, false /* removeOnly */)

结合我们的例子，我们的场景是首次渲染，所以在执行 patch 函数的时候，传入的 vm.$el 对应的是例子中 id 为 app 的 DOM 对象，这个也就是我们在 index.html 模板中写的 <div id="app">， vm.$el 的赋值是在之前 mountComponent 函数做的，vnode 对应的是调用 render 函数的返回值，hydrating 在非服务端渲染情况下为 false，removeOnly 为 false。

确定了这些入参后，我们回到 patch 函数的执行过程，看几个关键步骤。

```
const isRealElement = isDef(oldVnode.nodeType)

if (!isRealElement && sameVnode(oldVnode, vnode)) {

 // patch existing root node

 patchVnode(oldVnode, vnode, insertedVnodeQueue, removeOnly)

} else {

 if (isRealElement) {

  // mounting to a real element

  // check if this is server-rendered content and if we can perform

  // a successful hydration.

  if (oldVnode.nodeType === 1 && oldVnode.hasAttribute(SSR_ATTR)) {

   oldVnode.removeAttribute(SSR_ATTR)

   hydrating = true

  }

  if (isTrue(hydrating)) {

   if (hydrate(oldVnode, vnode, insertedVnodeQueue)) {

​    invokeInsertHook(vnode, insertedVnodeQueue, true)

​    return oldVnode

   } else if (process.env.NODE_ENV !== 'production') {

​    warn(

​     'The client-side rendered virtual DOM tree is not matching ' +

​     'server-rendered content. This is likely caused by incorrect ' +

​     'HTML markup, for example nesting block-level elements inside ' +

​     '<p>, or missing <tbody>. Bailing hydration and performing ' +

​     'full client-side render.'

​    )

   }

  }    

  // either not server-rendered, or hydration failed.

  // create an empty node and replace it

  oldVnode = emptyNodeAt(oldVnode)

 }

 

 // replacing existing element

 const oldElm = oldVnode.elm

 const parentElm = nodeOps.parentNode(oldElm)

 

 // create new node

 createElm(

  vnode,

  insertedVnodeQueue,

  // extremely rare edge case: do not insert if old element is in a

  // leaving transition. Only happens when combining transition +

  // keep-alive + HOCs. (#4590)

  oldElm._leaveCb ? null : parentElm,

  nodeOps.nextSibling(oldElm)

 )

}
```

由于我们传入的 oldVnode 实际上是一个 DOM container，所以 isRealElement 为 true，接下来又通过 emptyNodeAt 方法把 oldVnode 转换成 VNode 对象，然后再调用 createElm 方法，这个方法在这里非常重要，来看一下它的实现：

```
function createElm (

 vnode,

 insertedVnodeQueue,

 parentElm,

 refElm,

 nested,

 ownerArray,

 index

) {

 if (isDef(vnode.elm) && isDef(ownerArray)) {

  // This vnode was used in a previous render!

  // now it's used as a new node, overwriting its elm would cause

  // potential patch errors down the road when it's used as an insertion

  // reference node. Instead, we clone the node on-demand before creating

  // associated DOM element for it.

  vnode = ownerArray[index] = cloneVNode(vnode)

 }

 

 vnode.isRootInsert = !nested // for transition enter check

 if (createComponent(vnode, insertedVnodeQueue, parentElm, refElm)) {

  return

 }

 

 const data = vnode.data

 const children = vnode.children

 const tag = vnode.tag

 if (isDef(tag)) {

  if (process.env.NODE_ENV !== 'production') {

   if (data && data.pre) {

​    creatingElmInVPre++

   }

   if (isUnknownElement(vnode, creatingElmInVPre)) {

​    warn(

​     'Unknown custom element: <' + tag + '> - did you ' +

​     'register the component correctly? For recursive components, ' +

​     'make sure to provide the "name" option.',

​     vnode.context

​    )

   }

  }

 

  vnode.elm = vnode.ns

   ? nodeOps.createElementNS(vnode.ns, tag)

   : nodeOps.createElement(tag, vnode)

  setScope(vnode)

 

  /* istanbul ignore if */

  if (__WEEX__) {

   // ...

  } else {

   createChildren(vnode, children, insertedVnodeQueue)

   if (isDef(data)) {

​    invokeCreateHooks(vnode, insertedVnodeQueue)

   }

   insert(parentElm, vnode.elm, refElm)

  }

 

  if (process.env.NODE_ENV !== 'production' && data && data.pre) {

   creatingElmInVPre--

  }

 } else if (isTrue(vnode.isComment)) {

  vnode.elm = nodeOps.createComment(vnode.text)

  insert(parentElm, vnode.elm, refElm)

 } else {

  vnode.elm = nodeOps.createTextNode(vnode.text)

  insert(parentElm, vnode.elm, refElm)

 }

}
```

createElm 的作用是通过虚拟节点创建真实的 DOM 并插入到它的父节点中。 我们来看一下它的一些关键逻辑，createComponent 方法目的是尝试创建子组件，这个逻辑在之后组件的章节会详细介绍，在当前这个 case 下它的返回值为 false；接下来判断 vnode 是否包含 tag，如果包含，先简单对 tag 的合法性在非生产环境下做校验，看是否是一个合法标签；然后再去调用平台 DOM 的操作去创建一个占位符元素。

```
vnode.elm = vnode.ns

 ? nodeOps.createElementNS(vnode.ns, tag)

 : nodeOps.createElement(tag, vnode)

接下来调用 createChildren 方法去创建子元素：

createChildren(vnode, children, insertedVnodeQueue)

 

function createChildren (vnode, children, insertedVnodeQueue) {

 if (Array.isArray(children)) {

  if (process.env.NODE_ENV !== 'production') {

   checkDuplicateKeys(children)

  }

  for (let i = 0; i < children.length; ++i) {

   createElm(children[i], insertedVnodeQueue, vnode.elm, null, true, children, i)

  }

 } else if (isPrimitive(vnode.text)) {

  nodeOps.appendChild(vnode.elm, nodeOps.createTextNode(String(vnode.text)))

 }

}
```

createChildren 的逻辑很简单，实际上是遍历子虚拟节点，递归调用 createElm，这是一种常用的深度优先的遍历算法，这里要注意的一点是在遍历过程中会把 vnode.elm 作为父容器的 DOM 节点占位符传入。

接着再调用 invokeCreateHooks 方法执行所有的 create 的钩子并把 vnode push 到 insertedVnodeQueue 中。

```
 if (isDef(data)) {

 invokeCreateHooks(vnode, insertedVnodeQueue)

}

 

function invokeCreateHooks (vnode, insertedVnodeQueue) {

 for (let i = 0; i < cbs.create.length; ++i) {

  cbs.create[i](emptyNode, vnode)

 }

 i = vnode.data.hook // Reuse variable

 if (isDef(i)) {

  if (isDef(i.create)) i.create(emptyNode, vnode)

  if (isDef(i.insert)) insertedVnodeQueue.push(vnode)

 }

}
```

最后调用 insert 方法把 DOM 插入到父节点中，因为是递归调用，子元素会优先调用 insert，所以整个 vnode 树节点的插入顺序是先子后父。来看一下 insert方法，它的定义在 src/core/vdom/patch.js 上。

```
insert(parentElm, vnode.elm, refElm)

 

function insert (parent, elm, ref) {

 if (isDef(parent)) {

  if (isDef(ref)) {

   if (ref.parentNode === parent) {

​    nodeOps.insertBefore(parent, elm, ref)

   }

  } else {

   nodeOps.appendChild(parent, elm)

  }

 }

}
```

insert 逻辑很简单，调用一些 nodeOps 把子节点插入到父节点中，这些辅助方法定义在 src/platforms/web/runtime/node-ops.js 中：

```
export function insertBefore (parentNode: Node, newNode: Node, referenceNode: Node) {

 parentNode.insertBefore(newNode, referenceNode)

}

 

export function appendChild (node: Node, child: Node) {

 node.appendChild(child)

}
```

其实就是调用原生 DOM 的 API 进行 DOM 操作，看到这里，很多同学恍然大悟，原来 Vue 是这样动态创建的 DOM。

在 createElm 过程中，如果 vnode 节点如果不包含 tag，则它有可能是一个注释或者纯文本节点，可以直接插入到父元素中。在我们这个例子中，最内层就是一个文本 vnode，它的 text 值取的就是之前的 this.message 的值 Hello Vue!。

再回到 patch 方法，首次渲染我们调用了 createElm 方法，这里传入的 parentElm 是 oldVnode.elm 的父元素， 在我们的例子是 id 为 #app div 的父元素，也就是 Body；实际上整个过程就是递归创建了一个完整的 DOM 树并插入到 Body 上。

最后，我们根据之前递归 createElm 生成的 vnode 插入顺序队列，执行相关的 insert 钩子函数，这部分内容我们之后会详细介绍。

## **总结**

那么至此我们从主线上把模板和数据如何渲染成最终的 DOM 的过程分析完毕了，我们可以通过下图更直观地看到从初始化 Vue 到最终渲染的整个过程。

<img src="../assets/new-vue.png"/>

我们这里只是分析了最简单和最基础的场景，在实际项目中，我们是把页面拆成很多组件的，Vue 另一个核心思

# **createComponent**

上一章我们在分析 createElement 的实现的时候，它最终会调用 _createElement 方法，其中有一段逻辑是对参数 tag 的判断，如果是一个普通的 html 标签，像上一章的例子那样是一个普通的 div，则会实例化一个普通 VNode 节点，否则通过 createComponent 方法创建一个组件 VNode。

```
if (typeof tag === 'string') {

 let Ctor

 ns = (context.$vnode && context.$vnode.ns) || config.getTagNamespace(tag)

 if (config.isReservedTag(tag)) {

  // platform built-in elements

  vnode = new VNode(

   config.parsePlatformTagName(tag), data, children,

   undefined, undefined, context

  )

 } else if (isDef(Ctor = resolveAsset(context.$options, 'components', tag))) {

  // component

  vnode = createComponent(Ctor, data, context, children, tag)

 } else {

  // unknown or unlisted namespaced elements

  // check at runtime because it may get assigned a namespace when its

  // parent normalizes children

  vnode = new VNode(

   tag, data, children,

   undefined, undefined, context

  )

 }

} else {

 // direct component options / constructor

 vnode = createComponent(tag, data, context, children)

}
```

在我们这一章传入的是一个 App 对象，它本质上是一个 Component 类型，那么它会走到上述代码的 else 逻辑，直接通过 createComponent 方法来创建 vnode。所以接下来我们来看一下 createComponent 方法的实现，它定义在 src/core/vdom/create-component.js 文件中：

```
export function createComponent (

 Ctor: Class<Component> | Function | Object | void,

 data: ?VNodeData,

 context: Component,

 children: ?Array<VNode>,

 tag?: string

): VNode | Array<VNode> | void {

 if (isUndef(Ctor)) {

  return

 }

 

 const baseCtor = context.$options._base

 

 // plain options object: turn it into a constructor

 if (isObject(Ctor)) {

  Ctor = baseCtor.extend(Ctor)

 }

 

 // if at this stage it's not a constructor or an async component factory,

 // reject.

 if (typeof Ctor !== 'function') {

  if (process.env.NODE_ENV !== 'production') {

   warn(`Invalid Component definition: ${String(Ctor)}`, context)

  }

  return

 }

 

 // async component

 let asyncFactory

 if (isUndef(Ctor.cid)) {

  asyncFactory = Ctor

  Ctor = resolveAsyncComponent(asyncFactory, baseCtor, context)

  if (Ctor === undefined) {

   // return a placeholder node for async component, which is rendered

   // as a comment node but preserves all the raw information for the node.

   // the information will be used for async server-rendering and hydration.

   return createAsyncPlaceholder(

​    asyncFactory,

​    data,

​    context,

​    children,

​    tag

   )

  }

 }

 

 data = data || {}

 

 // resolve constructor options in case global mixins are applied after

 // component constructor creation

 resolveConstructorOptions(Ctor)

 

 // transform component v-model data into props & events

 if (isDef(data.model)) {

  transformModel(Ctor.options, data)

 }

 

 // extract props

 const propsData = extractPropsFromVNodeData(data, Ctor, tag)

 

 // functional component

 if (isTrue(Ctor.options.functional)) {

  return createFunctionalComponent(Ctor, propsData, data, context, children)

 }

 

 // extract listeners, since these needs to be treated as

 // child component listeners instead of DOM listeners

 const listeners = data.on

 // replace with listeners with .native modifier

 // so it gets processed during parent component patch.

 data.on = data.nativeOn

 

 if (isTrue(Ctor.options.abstract)) {

  // abstract components do not keep anything

  // other than props & listeners & slot

 

  // work around flow

  const slot = data.slot

  data = {}

  if (slot) {

   data.slot = slot

  }

 }

 

 // install component management hooks onto the placeholder node

 installComponentHooks(data)

 

 // return a placeholder vnode

 const name = Ctor.options.name || tag

 const vnode = new VNode(

  `vue-component-${Ctor.cid}${name ? `-${name}` : ''}`,

  data, undefined, undefined, undefined, context,

  { Ctor, propsData, listeners, tag, children },

  asyncFactory

 )

 

 // Weex specific: invoke recycle-list optimized @render function for

 // extracting cell-slot template.

 // https://github.com/Hanks10100/weex-native-directive/tree/master/component

 /* istanbul ignore if */

 if (__WEEX__ && isRecyclableComponent(vnode)) {

  return renderRecyclableComponentTemplate(vnode)

 }

 

 return vnode

}
```

可以看到，createComponent 的逻辑也会有一些复杂，但是分析源码比较推荐的是只分析核心流程，分支流程可以之后针对性的看，所以这里针对组件渲染这个 case 主要就 3 个关键步骤：

构造子类构造函数，安装组件钩子函数和实例化 vnode。

## **构造子类构造函数**

```
const baseCtor = context.$options._base

 

// plain options object: turn it into a constructor

if (isObject(Ctor)) {

Ctor = baseCtor.extend(Ctor)

}

我们在编写一个组件的时候，通常都是创建一个普通对象，还是以我们的 App.vue 为例，代码如下：

import HelloWorld from './components/HelloWorld'

 

export default {

 name: 'app',

 components: {

  HelloWorld

 }

}
```

这里 export 的是一个对象，所以 createComponent 里的代码逻辑会执行到 baseCtor.extend(Ctor)，在这里 baseCtor 实际上就是 Vue，这个的定义是在最开始初始化 Vue 的阶段，在 src/core/global-api/index.js 中的 initGlobalAPI 函数有这么一段逻辑：

// this is used to identify the "base" constructor to extend all plain-object

// components with in Weex's multi-instance scenarios.

Vue.options._base = Vue

细心的同学会发现，这里定义的是 Vue.option，而我们的 createComponent 取的是 context.$options，实际上在 src/core/instance/init.js 里 Vue 原型上的 _init 函数中有这么一段逻辑：

```
vm.$options = mergeOptions(

 resolveConstructorOptions(vm.constructor),

 options || {},

 vm

)
```

这样就把 Vue 上的一些 option 扩展到了 vm.$option 上，所以我们也就能通过 vm.$options._base 拿到 Vue 这个构造函数了。mergeOptions 的实现我们会在后续章节中具体分析，现在只需要理解它的功能是把 Vue 构造函数的 options 和用户传入的 options 做一层合并，到 vm.$options 上。

在了解了 baseCtor 指向了 Vue 之后，我们来看一下 Vue.extend 函数的定义，在 src/core/global-api/extend.js 中。

```
/**

 \* Class inheritance

 */

Vue.extend = function (extendOptions: Object): Function {

 extendOptions = extendOptions || {}

 const Super = this

 const SuperId = Super.cid

 const cachedCtors = extendOptions._Ctor || (extendOptions._Ctor = {})

 if (cachedCtors[SuperId]) {

  return cachedCtors[SuperId]

 }

 

 const name = extendOptions.name || Super.options.name

 if (process.env.NODE_ENV !== 'production' && name) {

  validateComponentName(name)

 }

 

 const Sub = function VueComponent (options) {

  this._init(options)

 }

 Sub.prototype = Object.create(Super.prototype)

 Sub.prototype.constructor = Sub

 Sub.cid = cid++

 Sub.options = mergeOptions(

  Super.options,

  extendOptions

 )

 Sub['super'] = Super

 

 // For props and computed properties, we define the proxy getters on

 // the Vue instances at extension time, on the extended prototype. This

 // avoids Object.defineProperty calls for each instance created.

 if (Sub.options.props) {

  initProps(Sub)

 }

 if (Sub.options.computed) {

  initComputed(Sub)

 }

 

 // allow further extension/mixin/plugin usage

 Sub.extend = Super.extend

 Sub.mixin = Super.mixin

 Sub.use = Super.use

 

 // create asset registers, so extended classes

 // can have their private assets too.

 ASSET_TYPES.forEach(function (type) {

  Sub[type] = Super[type]

 })

 // enable recursive self-lookup

 if (name) {

  Sub.options.components[name] = Sub

 }

 

 // keep a reference to the super options at extension time.

 // later at instantiation we can check if Super's options have

 // been updated.

 Sub.superOptions = Super.options

 Sub.extendOptions = extendOptions

 Sub.sealedOptions = extend({}, Sub.options)

 

 // cache constructor

 cachedCtors[SuperId] = Sub

 return Sub

}
```

Vue.extend 的作用就是构造一个 Vue 的子类，它使用一种非常经典的原型继承的方式把一个纯对象转换一个继承于 Vue 的构造器 Sub 并返回，然后对 Sub 这个对象本身扩展了一些属性，如扩展 options、添加全局 API 等；并且对配置中的 props 和 computed 做了初始化工作；最后对于这个 Sub 构造函数做了缓存，避免多次执行 Vue.extend 的时候对同一个子组件重复构造。

这样当我们去实例化 Sub 的时候，就会执行 this._init 逻辑再次走到了 Vue 实例的初始化逻辑，实例化子组件的逻辑在之后的章节会介绍。

const Sub = function VueComponent (options) {

 this._init(options)

}

## **安装组件钩子函数**

// install component management hooks onto the placeholder node

installComponentHooks(data)

我们之前提到 Vue.js 使用的 Virtual DOM 参考的是开源库 [snabbdom](https://github.com/snabbdom/snabbdom)，它的一个特点是在 VNode 的 patch 流程中对外暴露了各种时机的钩子函数，方便我们做一些额外的事情，Vue.js 也是充分利用这一点，在初始化一个 Component 类型的 VNode 的过程中实现了几个钩子函数：

```
const componentVNodeHooks = {

 init (vnode: VNodeWithData, hydrating: boolean): ?boolean {

  if (

   vnode.componentInstance &&

   !vnode.componentInstance._isDestroyed &&

   vnode.data.keepAlive

  ) {

   // kept-alive components, treat as a patch

   const mountedNode: any = vnode // work around flow

   componentVNodeHooks.prepatch(mountedNode, mountedNode)

  } else {

   const child = vnode.componentInstance = createComponentInstanceForVnode(

​    vnode,

​    activeInstance

   )

   child.$mount(hydrating ? vnode.elm : undefined, hydrating)

  }

 },

 

 prepatch (oldVnode: MountedComponentVNode, vnode: MountedComponentVNode) {

  const options = vnode.componentOptions

  const child = vnode.componentInstance = oldVnode.componentInstance

  updateChildComponent(

   child,

   options.propsData, // updated props

   options.listeners, // updated listeners

   vnode, // new parent vnode

   options.children // new children

  )

 },

 

 insert (vnode: MountedComponentVNode) {

  const { context, componentInstance } = vnode

  if (!componentInstance._isMounted) {

   componentInstance._isMounted = true

   callHook(componentInstance, 'mounted')

  }

  if (vnode.data.keepAlive) {

   if (context._isMounted) {

​    // vue-router#1212

​    // During updates, a kept-alive component's child components may

​    // change, so directly walking the tree here may call activated hooks

​    // on incorrect children. Instead we push them into a queue which will

​    // be processed after the whole patch process ended.

​    queueActivatedComponent(componentInstance)

   } else {

​    activateChildComponent(componentInstance, true /* direct */)

   }

  }

 },

 

 destroy (vnode: MountedComponentVNode) {

  const { componentInstance } = vnode

  if (!componentInstance._isDestroyed) {

   if (!vnode.data.keepAlive) {

​    componentInstance.$destroy()

   } else {

​    deactivateChildComponent(componentInstance, true /* direct */)

   }

  }

 }

}

 

const hooksToMerge = Object.keys(componentVNodeHooks)

 

function installComponentHooks (data: VNodeData) {

 const hooks = data.hook || (data.hook = {})

 for (let i = 0; i < hooksToMerge.length; i++) {

  const key = hooksToMerge[i]

  const existing = hooks[key]

  const toMerge = componentVNodeHooks[key]

  if (existing !== toMerge && !(existing && existing._merged)) {

   hooks[key] = existing ? mergeHook(toMerge, existing) : toMerge

  }

 }

}

 

function mergeHook (f1: any, f2: any): Function {

 const merged = (a, b) => {

  // flow complains about extra args which is why we use any

  f1(a, b)

  f2(a, b)

 }

 merged._merged = true

 return merged

}
```

整个 installComponentHooks 的过程就是把 componentVNodeHooks 的钩子函数合并到 data.hook 中，在 VNode 执行 patch 的过程中执行相关的钩子函数，具体的执行我们稍后在介绍 patch 过程中会详细介绍。这里要注意的是合并策略，在合并过程中，如果某个时机的钩子已经存在 data.hook 中，那么通过执行 mergeHook 函数做合并，这个逻辑很简单，就是在最终执行的时候，依次执行这两个钩子函数即可。

## **实例化 VNode**

const name = Ctor.options.name || tag

const vnode = new VNode(

`vue-component-${Ctor.cid}${name ? `-${name}` : ''}`,

data, undefined, undefined, undefined, context,

{ Ctor, propsData, listeners, tag, children },

asyncFactory

)

return vnode

最后一步非常简单，通过 new VNode 实例化一个 vnode 并返回。需要注意的是和普通元素节点的 vnode 不同，组件的 vnode 是没有 children 的，这点很关键，在之后的 patch 过程中我们会再提。

## **总结**

这一节我们分析了 createComponent 的实现，了解到它在渲染一个组件的时候的 3 个关键逻辑：构造子类构造函数，安装组件钩子函数和实例化 vnode。createComponent 后返回的是组件 vnode，它也一样走到 vm._update 方法，进而执行了 patch 函数，我们在上一章对 patch 函数做了简单的分析，那么下一节我们会对它做进一步的分析。

# **patch**

通过前一章的分析我们知道，当我们通过 createComponent 创建了组件 VNode，接下来会走到 vm._update，执行 vm.__patch__ 去把 VNode 转换成真正的 DOM 节点。这个过程我们在前一章已经分析过了，但是针对一个普通的 VNode 节点，接下来我们来看看组件的 VNode 会有哪些不一样的地方。

patch 的过程会调用 createElm 创建元素节点，回顾一下 createElm 的实现，它的定义在 src/core/vdom/patch.js 中：

```
function createElm (

 vnode,

 insertedVnodeQueue,

 parentElm,

 refElm,

 nested,

 ownerArray,

 index

) {

 // ...

 if (createComponent(vnode, insertedVnodeQueue, parentElm, refElm)) {

  return

 }

 // ...

}
```

## **createComponent**

我们删掉多余的代码，只保留关键的逻辑，这里会判断 createComponent(vnode, insertedVnodeQueue, parentElm, refElm) 的返回值，如果为 true 则直接结束，那么接下来看一下 createComponent 方法的实现：

```
function createComponent (vnode, insertedVnodeQueue, parentElm, refElm) {

 let i = vnode.data

 if (isDef(i)) {

  const isReactivated = isDef(vnode.componentInstance) && i.keepAlive

  if (isDef(i = i.hook) && isDef(i = i.init)) {

   i(vnode, false /* hydrating */)

  }

  // after calling the init hook, if the vnode is a child component

  // it should've created a child instance and mounted it. the child

  // component also has set the placeholder vnode's elm.

  // in that case we can just return the element and be done.

  if (isDef(vnode.componentInstance)) {

   initComponent(vnode, insertedVnodeQueue)

   insert(parentElm, vnode.elm, refElm)

   if (isTrue(isReactivated)) {

​    reactivateComponent(vnode, insertedVnodeQueue, parentElm, refElm)

   }

   return true

  }

 }

}

createComponent 函数中，首先对 vnode.data 做了一些判断：

let i = vnode.data

if (isDef(i)) {

 // ...

 if (isDef(i = i.hook) && isDef(i = i.init)) {

  i(vnode, false /* hydrating */)

  // ...

 }

 // ..

}
```

如果 vnode 是一个组件 VNode，那么条件会满足，并且得到 i 就是 init 钩子函数，回顾上节我们在创建组件 VNode 的时候合并钩子函数中就包含 init 钩子函数，定义在 src/core/vdom/create-component.js 中：

```
init (vnode: VNodeWithData, hydrating: boolean): ?boolean {

 if (

  vnode.componentInstance &&

  !vnode.componentInstance._isDestroyed &&

  vnode.data.keepAlive

 ) {

  // kept-alive components, treat as a patch

  const mountedNode: any = vnode // work around flow

  componentVNodeHooks.prepatch(mountedNode, mountedNode)

 } else {

  const child = vnode.componentInstance = createComponentInstanceForVnode(

   vnode,

   activeInstance

  )

  child.$mount(hydrating ? vnode.elm : undefined, hydrating)

 }

},
```

init 钩子函数执行也很简单，我们先不考虑 keepAlive 的情况，它是通过 createComponentInstanceForVnode 创建一个 Vue 的实例，然后调用 $mount 方法挂载子组件，
先来看一下 createComponentInstanceForVnode 的实现：

```
export function createComponentInstanceForVnode (

 vnode: any, // we know it's MountedComponentVNode but flow doesn't

 parent: any, // activeInstance in lifecycle state

): Component {

 const options: InternalComponentOptions = {

  _isComponent: true,

  _parentVnode: vnode,

  parent

 }

 // check inline-template render functions

 const inlineTemplate = vnode.data.inlineTemplate

 if (isDef(inlineTemplate)) {

  options.render = inlineTemplate.render

  options.staticRenderFns = inlineTemplate.staticRenderFns

 }

 return new vnode.componentOptions.Ctor(options)

}
```

createComponentInstanceForVnode 函数构造的一个内部组件的参数，然后执行 new vnode.componentOptions.Ctor(options)。这里的 vnode.componentOptions.Ctor 对应的就是子组件的构造函数，我们上一节分析了它实际上是继承于 Vue 的一个构造器 Sub，相当于 new Sub(options) 这里有几个关键参数要注意几个点，_isComponent 为 true 表示它是一个组件，parent 表示当前激活的组件实例（注意，这里比较有意思的是如何拿到组件实例，后面会介绍。

所以子组件的实例化实际上就是在这个时机执行的，并且它会执行实例的 _init 方法，这个过程有一些和之前不同的地方需要挑出来说，代码在 src/core/instance/init.js 中：

```
Vue.prototype._init = function (options?: Object) {

 const vm: Component = this

 // merge options

 if (options && options._isComponent) {

  // optimize internal component instantiation

  // since dynamic options merging is pretty slow, and none of the

  // internal component options needs special treatment.

  initInternalComponent(vm, options)

 } else {

  vm.$options = mergeOptions(

   resolveConstructorOptions(vm.constructor),

   options || {},

   vm

  )

 }

 // ...

 if (vm.$options.el) {

  vm.$mount(vm.$options.el)

 } 

}
```

这里首先是合并 options 的过程有变化，_isComponent 为 true，所以走到了 initInternalComponent 过程，这个函数的实现也简单看一下：

```
export function initInternalComponent (vm: Component, options: InternalComponentOptions) {

 const opts = vm.$options = Object.create(vm.constructor.options)

 // doing this because it's faster than dynamic enumeration.

 const parentVnode = options._parentVnode

 opts.parent = options.parent

 opts._parentVnode = parentVnode

 

 const vnodeComponentOptions = parentVnode.componentOptions

 opts.propsData = vnodeComponentOptions.propsData

 opts._parentListeners = vnodeComponentOptions.listeners

 opts._renderChildren = vnodeComponentOptions.children

 opts._componentTag = vnodeComponentOptions.tag

 

 if (options.render) {

  opts.render = options.render

  opts.staticRenderFns = options.staticRenderFns

 }

}
```

这个过程我们重点记住以下几个点即可：opts.parent = options.parent、opts._parentVnode = parentVnode，它们是把之前我们通过 createComponentInstanceForVnode 函数传入的几个参数合并到内部的选项 $options 里了。

再来看一下 _init 函数最后执行的代码：

if (vm.$options.el) {

  vm.$mount(vm.$options.el)

}

由于组件初始化的时候是不传 el 的，因此组件是自己接管了 $mount 的过程，这个过程的主要流程在上一章介绍过了，回到组件 init 的过程，componentVNodeHooks 的 init 钩子函数，在完成实例化的 _init 后，接着会执行 child.$mount(hydrating ? vnode.elm : undefined, hydrating) 。这里 hydrating 为 true 一般是服务端渲染的情况，我们只考虑客户端渲染，所以这里 $mount 相当于执行 child.$mount(undefined, false)，它最终会调用 mountComponent 方法，进而执行 vm._render() 方法：

```
Vue.prototype._render = function (): VNode {

 const vm: Component = this

 const { render, _parentVnode } = vm.$options

 

 // set parent vnode. this allows render functions to have access

 // to the data on the placeholder node.

 vm.$vnode = _parentVnode

 // render self

 let vnode

 try {

  vnode = render.call(vm._renderProxy, vm.$createElement)

 } catch (e) {

  // ...

 }

 // set parent

 vnode.parent = _parentVnode

 return vnode

}
```

我们只保留关键部分的代码，这里的 _parentVnode 就是当前组件的父 VNode，而 render 函数生成的 vnode 当前组件的渲染 vnode，vnode 的 parent 指向了 _parentVnode，也就是 vm.$vnode，它们是一种父子的关系。

我们知道在执行完 vm._render 生成 VNode 后，接下来就要执行 vm._update 去渲染 VNode 了。来看一下组件渲染的过程中有哪些需要注意的，vm._update 的定义在 src/core/instance/lifecycle.js 中：

```
export let activeInstance: any = null

Vue.prototype._update = function (vnode: VNode, hydrating?: boolean) {

 const vm: Component = this

 const prevEl = vm.$el

 const prevVnode = vm._vnode

 const prevActiveInstance = activeInstance

 activeInstance = vm

 vm._vnode = vnode

 // Vue.prototype.__patch__ is injected in entry points

 // based on the rendering backend used.

 if (!prevVnode) {

  // initial render

  vm.$el = vm.__patch__(vm.$el, vnode, hydrating, false /* removeOnly */)

 } else {

  // updates

  vm.$el = vm.__patch__(prevVnode, vnode)

 }

 activeInstance = prevActiveInstance

 // update __vue__ reference

 if (prevEl) {

  prevEl.__vue__ = null

 }

 if (vm.$el) {

  vm.$el.__vue__ = vm

 }

 // if parent is an HOC, update its $el as well

 if (vm.$vnode && vm.$parent && vm.$vnode === vm.$parent._vnode) {

  vm.$parent.$el = vm.$el

 }

 // updated hook is called by the scheduler to ensure that children are

 // updated in a parent's updated hook.

}

_update 过程中有几个关键的代码，首先 vm._vnode = vnode 的逻辑，这个 vnode 是通过 vm._render() 返回的组件渲染 VNode，vm._vnode 和 vm.$vnode 的关系就是一种父子关系，用代码表达就是 vm._vnode.parent === vm.$vnode。还有一段比较有意思的代码：

export let activeInstance: any = null

Vue.prototype._update = function (vnode: VNode, hydrating?: boolean) {

  // ...

  const prevActiveInstance = activeInstance

  activeInstance = vm

  if (!prevVnode) {

   // initial render

   vm.$el = vm.__patch__(vm.$el, vnode, hydrating, false /* removeOnly */)

  } else {

   // updates

   vm.$el = vm.__patch__(prevVnode, vnode)

  }

  activeInstance = prevActiveInstance

 }
```

这个 activeInstance 作用就是保持当前上下文的 Vue 实例，它是在 lifecycle 模块的全局变量，定义是 export let activeInstance: any = null，并且在之前我们调用 createComponentInstanceForVnode 方法的时候从 lifecycle 模块获取，并且作为参数传入的。因为实际上 JavaScript 是一个单线程，Vue 整个初始化是一个深度遍历的过程，在实例化子组件的过程中，它需要知道当前上下文的 Vue 实例是什么，并把它作为子组件的父 Vue 实例。之前我们提到过对子组件的实例化过程先会调用 initInternalComponent(vm, options) 合并 options，把 parent 存储在 vm.$options 中，在 $mount 之前会调用 initLifecycle(vm)方法：

```
export function initLifecycle (vm: Component) {

 const options = vm.$options

 

 // locate first non-abstract parent

 let parent = options.parent

 if (parent && !options.abstract) {

  while (parent.$options.abstract && parent.$parent) {

   parent = parent.$parent

  }

  parent.$children.push(vm)

 }

 

 vm.$parent = parent

 // ...

}
```

可以看到 vm.$parent 就是用来保留当前 vm 的父实例，并且通过 parent.$children.push(vm) 来把当前的 vm 存储到父实例的 $children 中。

在 vm._update 的过程中，把当前的 vm 赋值给 activeInstance，同时通过 const prevActiveInstance = activeInstance 用 prevActiveInstance 保留上一次的 activeInstance。实际上，prevActiveInstance 和当前的 vm 是一个父子关系，当一个 vm 实例完成它的所有子树的 patch 或者 update 过程后，activeInstance 会回到它的父实例，这样就完美地保证了 createComponentInstanceForVnode 整个深度遍历过程中，我们在实例化子组件的时候能传入当前子组件的父 Vue 实例，并在 _init 的过程中，通过 vm.$parent 把这个父子关系保留。

那么回到 _update，最后就是调用 __patch__ 渲染 VNode 了。

```
vm.$el = vm.__patch__(vm.$el, vnode, hydrating, false /* removeOnly */)

 

function patch (oldVnode, vnode, hydrating, removeOnly) {

 // ...

 let isInitialPatch = false

 const insertedVnodeQueue = []

 

 if (isUndef(oldVnode)) {

  // empty mount (likely as component), create new root element

  isInitialPatch = true

  createElm(vnode, insertedVnodeQueue)

 } else {

  // ...

 }

 // ...

}
```

这里又回到了本节开始的过程，之前分析过负责渲染成 DOM 的函数是 createElm，注意这里我们只传了 2 个参数，所以对应的 parentElm 是 undefined。我们再来看看它的定义：

```
function createElm (

 vnode,

 insertedVnodeQueue,

 parentElm,

 refElm,

 nested,

 ownerArray,

 index

) {

 // ...

 if (createComponent(vnode, insertedVnodeQueue, parentElm, refElm)) {

  return

 }

 

 const data = vnode.data

 const children = vnode.children

 const tag = vnode.tag

 if (isDef(tag)) {

  // ...

 

  vnode.elm = vnode.ns

   ? nodeOps.createElementNS(vnode.ns, tag)

   : nodeOps.createElement(tag, vnode)

  setScope(vnode)

 

  /* istanbul ignore if */

  if (__WEEX__) {

   // ...

  } else {

   createChildren(vnode, children, insertedVnodeQueue)

   if (isDef(data)) {

​    invokeCreateHooks(vnode, insertedVnodeQueue)

   }

   insert(parentElm, vnode.elm, refElm)

  }

 

  // ...

 } else if (isTrue(vnode.isComment)) {

  vnode.elm = nodeOps.createComment(vnode.text)

  insert(parentElm, vnode.elm, refElm)

 } else {

  vnode.elm = nodeOps.createTextNode(vnode.text)

  insert(parentElm, vnode.elm, refElm)

 }

}
```

注意，这里我们传入的 vnode 是组件渲染的 vnode，也就是我们之前说的 vm._vnode，如果组件的根节点是个普通元素，那么 vm._vnode 也是普通的 vnode，这里 createComponent(vnode, insertedVnodeQueue, parentElm, refElm) 的返回值是 false。接下来的过程就和我们上一章一样了，先创建一个父节点占位符，然后再遍历所有子 VNode 递归调用 createElm，在遍历的过程中，如果遇到子 VNode 是一个组件的 VNode，则重复本节开始的过程，这样通过一个递归的方式就可以完整地构建了整个组件树。

由于我们这个时候传入的 parentElm 是空，所以对组件的插入，在 createComponent 有这么一段逻辑：

```
function createComponent (vnode, insertedVnodeQueue, parentElm, refElm) {

 let i = vnode.data

 if (isDef(i)) {

  // ....

  if (isDef(i = i.hook) && isDef(i = i.init)) {

   i(vnode, false /* hydrating */)

  }

  // ...

  if (isDef(vnode.componentInstance)) {

   initComponent(vnode, insertedVnodeQueue)

   insert(parentElm, vnode.elm, refElm)

   if (isTrue(isReactivated)) {

​    reactivateComponent(vnode, insertedVnodeQueue, parentElm, refElm)

   }

   return true

  }

 }

}
```

在完成组件的整个 patch 过程后，最后执行 insert(parentElm, vnode.elm, refElm) 完成组件的 DOM 插入，如果组件 patch 过程中又创建了子组件，那么DOM 的插入顺序是先子后父。

## **总结**

那么到此，一个组件的 VNode 是如何创建、初始化、渲染的过程也就介绍完毕了。在对组件化的实现有一个大概了解后，接下来我们来介绍一下这其中的一些细节。我们知道编写一个组件实际上是编写一个 JavaScript 对象，对象的描述就是各种配置，之前我们提到在 _init 的最初阶段执行的就是 merge options 的逻辑，那么下一节我们从源码角度来分析合并配置的过程。
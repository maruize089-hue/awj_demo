# 社区服务商城项目说明书

本文档面向后续开发者和 AI 编程助手，用于快速理解、运行、维护和继续开发本项目。项目当前由一个 Spring Boot 单体后端和一个微信小程序前端组成。

## 1. 项目总览

### 目录结构

```text
d:\PythonProject\xcx\awj
├── awj_backend/                    # 后端：Spring Boot 单体应用
│   └── awj/                        # 核心代码目录
│       ├── src/main/java/com/example/awj/
│       │   ├── controller/         # REST API 控制器
│       │   ├── service/            # 业务服务
│       │   ├── mapper/             # MyBatis-Plus Mapper
│       │   ├── entity/             # 数据库实体
│       │   ├── dto/                # 请求 DTO
│       │   ├── vo/                 # 响应 VO
│       │   ├── security/           # Spring Security、JWT 认证
│       │   ├── config/             # 配置类
│       │   └── common/             # 通用类
│       └── src/main/resources/
│           ├── application.yml     # 应用配置
│           ├── schema.sql          # 数据库初始化
│           └── data.sql            # 初始化数据
├── awj_miniprogram/                # 前端：微信小程序原生项目
├── prd.md                          # 产品需求文档
└── project.md                      # 当前项目说明文档
```

根目录当前不是 Git 仓库。维护时优先关注 `src` 源码、`build.gradle` 和小程序源码目录。
本文档已按当前 Windows 开发环境更新。后端配置支持环境变量覆盖，避免再次因为换机器修改源码。

### 系统定位

这是一个社区服务商城系统，整合**商城购物**与**上门服务**两大核心业务，主要提供三类入口：

- 用户端（小程序）：首页浏览、商品搜索、购物车、订单管理、退货退款、个人中心、地址管理、优惠券、消息通知、收藏、钱包。
- 商家端（API）：商品管理、服务管理、订单管理、退货退款管理、商家信息管理。
- 管理端（API）：商家审核、商品/服务审核、用户管理。

### 当前技术栈

后端：

- Java 17
- Spring Boot 3.2.x
- Spring Security + JWT
- Spring Data Redis
- Spring Validation
- MyBatis-Plus 3.5.x
- MySQL Connector/J 8.3.0
- JWT：`jjwt` 0.12.5
- Knife4j OpenAPI 4.4.0
- Lombok
- Hutool 5.8.25

前端（小程序）：

- 微信小程序原生开发
- 自定义 `tabBar`
- 接口请求统一封装在 `awj_miniprogram/utils/request.js`

## 2. 本地运行环境

### 后端运行

后端目录：

```text
awj_backend/awj
```

关键配置文件：

```text
awj_backend/awj/src/main/resources/application.yml
```

核心配置：

```yaml
server:
  port: ${SERVER_PORT:8080}

spring:
  datasource:
    url: ${MYSQL_URL:jdbc:mysql://localhost:3306/community_mall?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true}
    username: ${MYSQL_USERNAME:root}
    password: ${MYSQL_PASSWORD:123456}
  data:
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}
      password: ${REDIS_PASSWORD:}
      database: ${REDIS_DATABASE:0}

jwt:
  secret: ${JWT_SECRET:community-mall-jwt-secret-key-2026}
  expiration: ${JWT_EXPIRATION:604800000}

wx:
  pay:
    app-id: ${WX_PAY_APP_ID:}
    mch-id: ${WX_PAY_MCH_ID:}
    api-key: ${WX_PAY_API_KEY:}
```

当前 Windows 本地依赖：

- Docker MySQL 容器：`mysql8`
- MySQL：`localhost:3306`
- 数据库名：`community_mall`
- MySQL 用户名/密码：`root` / `123456`
- Docker Redis 容器：`redis`
- Redis：`localhost:6379`
- JDK：17
- Gradle：项目自带 Gradle Wrapper

推荐启动顺序：

1. 启动 Docker Desktop。
2. 确认 MySQL、Redis 容器正在运行：

```bash
docker ps
docker exec redis redis-cli ping
```

3. 如果 `community_mall` 数据库不存在或为空，执行初始化 SQL：

```bash
docker cp awj_backend/awj/src/main/resources/schema.sql mysql8:/tmp/mall-schema.sql
docker cp awj_backend/awj/src/main/resources/data.sql mysql8:/tmp/mall-data.sql
docker exec mysql8 sh -c 'mysql --default-character-set=utf8mb4 -uroot -p123456 < /tmp/mall-schema.sql'
docker exec mysql8 sh -c 'mysql --default-character-set=utf8mb4 -uroot -p123456 < /tmp/mall-data.sql'
```

4. 校验数据是否已导入：

```bash
docker exec mysql8 mysql -uroot -p123456 community_mall -e 'SELECT COUNT(*) AS users FROM awj_user; SELECT COUNT(*) AS products FROM awj_product;'
```

5. 用 IDEA 打开 `awj_backend/awj`，运行 `AwjApplication.java`。

打包命令：

```bash
./gradlew clean build
```

### 小程序运行

前端目录：

```text
awj_miniprogram
```

使用微信开发者工具导入该目录。项目配置文件：

```text
awj_miniprogram/project.config.json
```

当前小程序接口基地址：

```js
const BASE_URL = 'http://localhost:8080';
```

配置位置：

```text
awj_miniprogram/utils/request.js
```

本地调试时需要后端已在 `8080` 端口启动。

默认初始化账号：

- 管理员：`admin` / `123456`
- 用户：`test` / `123456`
- 商家：`merchant1` / `123456`

## 3. 后端结构

### 包结构

```text
src/main/java/com/example/awj
├── AwjApplication.java             # Spring Boot 启动类
├── common/                         # 通用结果、异常、处理器、工具
│   ├── exception/                  # 全局异常处理
│   │   └── GlobalExceptionHandler.java
│   ├── handler/                    # MyBatis-Plus 自动填充
│   │   └── MyMetaObjectHandler.java
│   ├── result/                     # 统一响应封装
│   │   ├── Result.java
│   │   └── PageResult.java
│   └── utils/                      # 工具类
│       └── JwtUtils.java
├── config/                         # Web、Redis、MyBatis-Plus、Knife4j、CORS 配置
├── controller/                     # REST API 控制器
│   ├── admin/                      # 管理端接口
│   │   ├── BannerManageController.java
│   │   ├── CategoryManageController.java
│   │   ├── EvaluationManageController.java
│   │   └── MerchantManageController.java
│   ├── AuthController.java         # 认证接口
│   ├── UserController.java         # 用户接口
│   ├── ProductController.java      # 商品接口
│   ├── ServiceController.java      # 服务接口
│   ├── CategoryController.java     # 分类接口
│   ├── BannerController.java       # Banner接口
│   ├── CartController.java         # 购物车接口
│   ├── OrderController.java        # 订单接口
│   ├── RefundController.java       # 退货退款接口
│   ├── ReviewController.java       # 评价接口
│   ├── CouponController.java       # 优惠券接口
│   ├── FavoriteController.java     # 收藏接口
│   ├── MessageController.java      # 消息接口
│   ├── MerchantController.java     # 商家接口
│   ├── WalletController.java       # 钱包接口
│   └── AddressController.java      # 地址接口
├── dto/                            # 请求 DTO
├── entity/                         # 数据库实体
├── mapper/                         # MyBatis-Plus Mapper
├── security/                       # Spring Security、JWT 认证
│   ├── SecurityConfig.java         # 安全配置（含角色权限）
│   ├── JwtAuthenticationFilter.java
│   ├── UserDetailsServiceImpl.java
│   └── LoginUser.java
├── service/                        # Service 接口
│   └── impl/                       # Service 实现类
└── vo/                             # 响应 VO
```

### 统一响应结构

普通接口使用：

```text
com.example.awj.common.result.Result<T>
```

字段：

- `code`
- `message`
- `data`
- `timestamp`

分页数据使用：

```text
com.example.awj.common.result.PageResult<T>
```

字段：

- `total`
- `pages`
- `current`
- `size`
- `records`

常见成功码为 `200`。

### 认证与登录

认证接口：

```text
POST /api/auth/login
POST /api/auth/register
POST /api/auth/wx-login
POST /api/auth/logout
GET /api/auth/health
```

登录成功后后端返回 JWT、小程序缓存 `token`、`userInfo` 和 `role`。后续请求由 `utils/request.js` 自动携带：

```http
Authorization: Bearer <token>
Content-Type: application/json
```

相关代码：

- `security/SecurityConfig.java`
- `security/JwtAuthenticationFilter.java`
- `security/UserDetailsServiceImpl.java`
- `security/LoginUser.java`
- `service/UserService.java`

当前放行路径：

- `/api/auth/login`
- `/api/auth/register`
- `/api/auth/wx-login`
- `/api/auth/health`
- `/api/banner/list`
- `/api/category/list`
- `/api/product/list`、`/api/product/{id}`
- `/api/service/list`、`/api/service/{id}`
- `/api/review/list`
- `/doc.html`、`/swagger-ui/**`、`/v3/api-docs/**`、`/webjars/**`

角色权限路径：

- `/api/admin/**`：`ROLE_ADMIN`
- `/api/merchant/**`：`ROLE_MERCHANT`
- `/api/user/**`：`ROLE_USER`
- `/api/cart/**`：`ROLE_USER`
- `/api/favorite/**`：`ROLE_USER`
- `/api/message/**`：`ROLE_USER`
- `/api/wallet/**`：`ROLE_USER`

### 接口分组

认证：

- `/api/auth`

用户端：

- `/api/user`
- `/api/product`（只读）
- `/api/service`（只读）
- `/api/category`
- `/api/cart`
- `/api/order`
- `/api/refund`（用户操作）
- `/api/message`
- `/api/favorite`
- `/api/wallet`
- `/api/coupon`

商家端：

- `/api/merchant`
- `/api/product`（增删改）
- `/api/service`（增删改）
- `/api/refund`（审核操作）

管理端：

- `/api/admin/**`

## 4. 当前 API 清单

### 认证接口

`AuthController`：

- `POST /api/auth/login`：登录
- `POST /api/auth/register`：注册
- `POST /api/auth/wx-login`：微信小程序登录
- `POST /api/auth/logout`：退出登录
- `GET /api/auth/health`：健康检查

### 用户端接口

`UserController`：

- `GET /api/user/info`：获取用户信息
- `GET /api/user/addresses`：获取地址列表
- `POST /api/user/addresses`：添加地址
- `PUT /api/user/addresses/{id}`：更新地址
- `DELETE /api/user/addresses/{id}`：删除地址
- `GET /api/user/coupons`：获取优惠券列表

`ProductController`（只读）：

- `GET /api/product/list`：商品列表
- `GET /api/product/{id}`：商品详情
- `GET /api/product/search`：搜索商品

`ServiceController`（只读）：

- `GET /api/service/list`：服务列表
- `GET /api/service/{id}`：服务详情
- `GET /api/service/search`：搜索服务

`CategoryController`：

- `GET /api/category/list`：分类列表

`BannerController`：

- `GET /api/banner/list`：Banner列表（首页轮播）

`CartController`：

- `GET /api/cart/list`：购物车列表
- `GET /api/cart/items/{itemIds}`：获取购物车商品
- `POST /api/cart/add`：添加购物车
- `POST /api/cart/update`：更新购物车
- `DELETE /api/cart/delete/{id}`：删除购物车
- `DELETE /api/cart/clear`：清空购物车

`OrderController`：

- `POST /api/order/create`：创建订单
- `GET /api/order/list`：订单列表
- `GET /api/order/{id}`：订单详情
- `GET /api/order/stats`：订单统计
- `POST /api/order/pay`：支付订单
- `POST /api/order/complete`：完成订单
- `POST /api/order/cancel`：取消订单

`RefundController`（用户）：

- `POST /api/refund`：申请退货退款
- `GET /api/refund/list`：退货退款列表
- `GET /api/refund/{id}`：退货退款详情

`ReviewController`：

- `GET /api/review/list`：评价列表
- `GET /api/review/check`：检查是否已评价
- `POST /api/review/create`：创建评价

`CouponController`：

- `GET /api/coupon/list`：优惠券列表
- `POST /api/coupon/use`：使用优惠券

`FavoriteController`：

- `GET /api/favorite/list`：收藏列表
- `GET /api/favorite/check`：检查是否已收藏
- `POST /api/favorite/add`：添加收藏
- `POST /api/favorite/remove`：移除收藏

`MessageController`：

- `GET /api/message/list`：消息列表
- `GET /api/message/{id}`：消息详情
- `PUT /api/message/{id}/read`：标记已读
- `PUT /api/message/read/all`：全部已读

`WalletController`：

- `GET /api/wallet/info`：钱包信息
- `GET /api/wallet/records`：钱包记录
- `POST /api/wallet/recharge`：充值
- `POST /api/wallet/withdraw`：提现

`AddressController`：

- `GET /api/address/list`：地址列表
- `GET /api/address/{id}`：地址详情
- `POST /api/address/create`：创建地址
- `POST /api/address/update`：更新地址
- `DELETE /api/address/delete/{id}`：删除地址

### 商家端接口

`MerchantController`：

- `POST /api/merchant/register`：商家注册
- `GET /api/merchant/info`：获取商家信息
- `PUT /api/merchant/info`：更新商家信息

`ProductController`（商家）：

- `POST /api/product`：添加商品
- `PUT /api/product/{id}`：更新商品
- `DELETE /api/product/{id}`：删除商品

`ServiceController`（商家）：

- `POST /api/service`：添加服务
- `PUT /api/service/{id}`：更新服务
- `DELETE /api/service/{id}`：删除服务

`RefundController`（商家）：

- `PUT /api/refund/{id}/audit`：审核退货申请
- `PUT /api/refund/{id}/pay`：发起退款

### 管理端接口

管理端接口通过 `/api/admin/**` 路径访问，需要 `ROLE_ADMIN` 角色。

`BannerManageController`：

- `POST /api/admin/banners`：添加 Banner
- `PUT /api/admin/banners/{id}`：更新 Banner
- `DELETE /api/admin/banners/{id}`：删除 Banner

`CategoryManageController`：

- `POST /api/admin/categories`：添加分类
- `PUT /api/admin/categories/{id}`：更新分类
- `DELETE /api/admin/categories/{id}`：删除分类

`EvaluationManageController`：

- `GET /api/admin/evaluations/list`：评价列表（分页）
- `DELETE /api/admin/evaluations/{id}`：删除评价

`MerchantManageController`：

- `GET /api/admin/merchants/list`：商家列表（分页）
- `GET /api/admin/merchants/{id}`：商家详情
- `PUT /api/admin/merchants/{id}/audit`：审核商家

## 5. 后端业务模块对应关系

认证和用户：

- Controller：`AuthController`、`UserController`、`AddressController`
- Service：`UserService`、`AddressService`
- 表：`awj_user`、`awj_address`

商家管理：

- Controller：`MerchantController`
- Service：`MerchantService`
- 表：`awj_merchant`

商品管理：

- Controller：`ProductController`
- Service：`ProductService`
- 表：`awj_product`、`awj_product_sku`

服务管理：

- Controller：`ServiceController`
- Service：`ServiceService`
- 表：`awj_service`

分类管理：

- Controller：`CategoryController`
- Service：`CategoryService`
- 表：`awj_category`

购物车：

- Controller：`CartController`
- Service：`CartService`
- 表：`awj_cart`

订单：

- Controller：`OrderController`
- Service：`OrderService`
- 表：`awj_order`、`awj_order_item`、`awj_product_order`、`awj_service_order`

> **注意**：`ReviewController` 对应 `EvaluationService`，使用的表是 `awj_evaluation`，存在命名不一致（Review vs Evaluation），后续开发需注意此映射关系。

退货退款：

- Controller：`RefundController`
- Service：`RefundService`
- 表：`awj_refund`

评价：

- Controller：`ReviewController`
- Service：`EvaluationService`
- 表：`awj_evaluation`

消息：

- Controller：`MessageController`
- Service：`MessageService`
- 表：`awj_message`

优惠券：

- Controller：`CouponController`
- Service：`CouponService`
- 表：`awj_coupon`、`awj_user_coupon`

收藏：

- Controller：`FavoriteController`
- Service：`FavoriteService`
- 表：`awj_favorite`

钱包：

- Controller：`WalletController`
- Service：`WalletService`
- 表：`awj_wallet`、`awj_wallet_record`

管理端：

- Controller：`BannerManageController`、`CategoryManageController`、`EvaluationManageController`、`MerchantManageController`
- Service：`BannerService`、`CategoryService`、`EvaluationService`、`MerchantService`
- 表：`awj_banner`、`awj_category`、`awj_evaluation`、`awj_merchant`

## 6. API 文档

后端集成 Knife4j。启动后可访问：

```text
http://localhost:8080/doc.html
http://localhost:8080/swagger-ui.html
http://localhost:8080/v3/api-docs
```

## 7. 前端结构

### 小程序结构

主要文件：

```text
awj_miniprogram
├── app.js
├── app.json
├── app.wxss
├── utils
│   ├── request.js       # 统一请求封装
│   ├── auth.js          # 认证工具
│   └── util.js          # 通用工具
└── pages
    ├── index/           # 首页
    ├── category/        # 分类页
    ├── cart/            # 购物车页
    ├── profile/         # 个人中心
    ├── detail/          # 详情页
    ├── order/           # 订单页
    ├── address/         # 地址管理
    ├── coupon/          # 优惠券页
    ├── favorite/        # 收藏页
    ├── review/          # 评价页
    ├── wallet/          # 钱包页
    └── help/            # 帮助页
```

请求封装：

统一请求文件：

```text
awj_miniprogram/utils/request.js
```

职责：

- 拼接 `BASE_URL`
- 默认超时 `60000ms`
- 从小程序缓存读取 `token`
- 自动添加 `Authorization: Bearer <token>`
- 自动添加 `Content-Type: application/json`
- 处理 HTTP `401` 和业务 `code === 401`
- 清理登录缓存并跳转登录页

## 8. 数据库说明

SQL 文件：

```text
awj_backend/awj/src/main/resources/schema.sql
awj_backend/awj/src/main/resources/data.sql
```

数据库名：

```text
community_mall
```

核心表：

用户与商家：

- `awj_user`：系统用户
- `awj_merchant`：商家信息
- `awj_address`：收货地址

商品与服务：

- `awj_category`：分类
- `awj_product`：商品
- `awj_product_sku`：商品SKU
- `awj_service`：服务

订单：

- `awj_order`：订单
- `awj_order_item`：订单明细
- `awj_cart`：购物车

退货退款：

- `awj_refund`：退货退款

评价与消息：

- `awj_evaluation`：评价
- `awj_message`：消息

优惠券：

- `awj_coupon`：优惠券
- `awj_user_coupon`：用户优惠券

收藏：

- `awj_favorite`：收藏

钱包：

- `awj_wallet`：钱包
- `awj_wallet_record`：钱包记录

Banner：

- `awj_banner`：轮播广告

## 9. 后续开发建议

### 新增后端接口

推荐路径：

1. 在 `entity` 中确认或新增数据库实体。
2. 在 `dto` 中新增请求对象。
3. 在 `vo` 中新增响应对象。
4. 在 `mapper` 中新增 MyBatis-Plus Mapper。
5. 在 `service` 中定义业务接口。
6. 在 `service/impl` 中实现业务逻辑。
7. 在 `controller` 中暴露 REST API。
8. 在小程序 `utils/request.js` 中同步封装请求方法。
9. 在对应小程序页面中调用 API。
10. 如涉及数据库结构或初始化数据，同步更新 `schema.sql` 和 `data.sql`。

### 新增小程序页面

推荐路径：

1. 在 `pages` 下新增页面目录。
2. 在 `app.json` 的 `pages` 中注册页面。
3. 需要接口时优先在 `utils/request.js` 中封装。
4. 页面 JS 只负责页面状态和交互，避免直接散落 `wx.request`。
5. 如果属于底部导航入口，同步检查 `app.json` 的 `tabBar.list`。

### 接口联动约定

后端新增或修改接口后，需要同步检查：

- 小程序 `utils/request.js` 是否已封装。
- 页面传参和后端 DTO 字段名是否一致。
- 响应是否仍符合 `Result<T>` 或 `PageResult<T>`。
- 分页参数是否和后端 `PageDto` 或控制器参数一致。
- 需要登录的接口是否携带 `Authorization` 请求头。
- 角色权限是否正确配置。

## 10. 当前风险与注意事项

### 1. 中文编码与数据库导入

数据库中文乱码问题需提前预防。根因可能是 SQL 导入或 MySQL 会话字符集按 latin1 处理 UTF-8 数据。

防复发措施：

- `schema.sql` 增加 `SET NAMES utf8mb4;`
- `data.sql` 增加 `SET NAMES utf8mb4;`
- 初始化命令使用 `mysql --default-character-set=utf8mb4`

### 2. 密码编码策略

`SecurityConfig` 当前使用：

```java
new BCryptPasswordEncoder()
```

`data.sql` 中初始化的 `admin`、`test` 和 `merchant1` 密码均为 bcrypt 哈希，明文默认密码为：

```text
123456
```

维护建议：

- 不要再把初始化密码改回明文。
- 注册、改密、后台新增用户应继续走 `PasswordEncoder.encode(...)`。
- 如果重置 Docker 数据库，重新执行 `schema.sql` 和 `data.sql` 即可恢复默认账号。

### 3. 角色权限配置

项目已在 `SecurityConfig` 中配置了角色权限路径匹配，并在控制器方法上添加了 `@PreAuthorize` 注解进行细粒度角色校验。

角色权限矩阵：

| 路径 | 角色要求 | 说明 |
|---|---|---|
| `/api/admin/**` | `ROLE_ADMIN` | 管理端接口 |
| `/api/admin/banners/**` | `ROLE_ADMIN` | Banner管理 |
| `/api/admin/categories/**` | `ROLE_ADMIN` | 分类管理 |
| `/api/admin/evaluations/**` | `ROLE_ADMIN` | 评价管理 |
| `/api/admin/merchants/**` | `ROLE_ADMIN` | 商家审核管理 |
| `/api/merchant/**` | `ROLE_MERCHANT` | 商家端接口 |
| `/api/user/**` | `ROLE_USER` | 用户个人信息 |
| `/api/cart/**` | `ROLE_USER` | 购物车 |
| `/api/favorite/**` | `ROLE_USER` | 收藏 |
| `/api/message/**` | `ROLE_USER` | 消息 |
| `/api/wallet/**` | `ROLE_USER` | 钱包 |
| `/api/address/**` | `ROLE_USER` | 地址管理 |
| `/api/product`（增删改） | `ROLE_MERCHANT` | 商家商品管理 |
| `/api/service`（增删改） | `ROLE_MERCHANT` | 商家服务管理 |
| `/api/refund`（审核） | `ROLE_MERCHANT` | 商家退款审核 |

### 4. 微信支付依赖外部配置

微信支付依赖 `WX_PAY_APP_ID`、`WX_PAY_MCH_ID`、`WX_PAY_API_KEY`。未配置时接口会抛出业务异常。演示支付功能前需要确认：

- 环境变量已配置。
- 后端运行进程能读取该环境变量。
- 微信支付网络访问正常。

### 5. 测试覆盖

项目已添加基础单元测试，位于 `src/test/java/com/example/awj/security/`：

- `JwtAuthenticationTest.java`：JWT 认证测试
- `RolePermissionTest.java`：角色权限测试

继续开发关键业务时建议补充：

- 商品订单、服务订单流程测试。
- 退货退款流程测试。
- 支付回调测试。

## 11. AI 接手提示

后续由 AI 或新开发者维护时，建议优先按这个顺序读代码：

1. `awj_backend/awj/build.gradle`
2. `awj_backend/awj/src/main/resources/application.yml`
3. `awj_backend/awj/src/main/java/com/example/awj/security/SecurityConfig.java`
4. `awj_backend/awj/src/main/java/com/example/awj/controller/`
5. `awj_backend/awj/src/main/resources/schema.sql`
6. `awj_backend/awj/src/main/resources/data.sql`
7. `awj_miniprogram/app.json`
8. `awj_miniprogram/utils/request.js`

维护原则：

- 不要只改后端接口而忘记同步小程序 API 封装。
- 不要只改页面字段而忘记检查后端 DTO、VO 和数据库字段。
- 数据库字段变更必须同步 `schema.sql`。
- 初始化数据变更必须同步 `data.sql`。
- 涉及登录、角色、密码、支付、订单、库存时要额外检查安全和边界条件。
- 本地后端默认端口是 `8080`。
- 小程序默认请求地址是 `http://localhost:8080`。
- MySQL 默认连接是 `localhost:3306/community_mall`，默认用户名/密码是 `root` / `123456`。
- Redis 默认连接是 `localhost:6379`。
- 初始化登录账号是 `admin/123456`、`test/123456` 和 `merchant1/123456`。

## 12. 快速核对清单

开发或交接前建议确认：

- Docker Desktop 已启动。
- MySQL 容器已启动并暴露 `3306`。
- 数据库 `community_mall` 已创建。
- `schema.sql` 和 `data.sql` 已执行。
- Redis 容器已启动并暴露 `6379`。
- 后端已启动，端口 `8080` 可访问。
- `http://localhost:8080/doc.html` 可打开。
- 小程序 `utils/request.js` 的 `BASE_URL` 指向当前后端。
- 登录后本地缓存中存在 `token`、`userInfo`、`role`。
- 修改接口后已同步检查后端 Controller 和小程序 API 封装。

---

**文档版本**: v1.7  
**创建日期**: 2026-07-06  
**最后更新**: 2026-07-07  
**适用项目**: 社区服务商城系统

**本次更新内容**:
- 补充了 `controller/admin/` 子目录下的4个管理端控制器及其API端点
- 完善了 `common/` 包结构，包含 `exception/`、`handler/`、`result/`、`utils/` 子包
- 在包结构中添加了 `service/impl/` 目录
- 在后端业务模块对应关系中添加了管理端模块
- 更新了管理端接口清单，列出了所有具体的API端点
- 补充了 `AddressController` 的API端点文档
- 更新了角色权限矩阵，添加了 `/api/address/**` 和具体的管理端子路径
- 在订单模块中补充了 `awj_product_order`、`awj_service_order` 表
- 添加了 `ReviewController` → `EvaluationService` 命名不一致的说明
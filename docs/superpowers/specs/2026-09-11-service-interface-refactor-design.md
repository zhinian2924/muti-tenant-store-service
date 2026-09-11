# Service 接口与实现分离设计

## 1. 背景与目标

当前项目中的多数应用服务以具体类直接提供业务能力。为统一 Service 层结构，本次重构将适合对调用方暴露的业务服务调整为“接口 + 实现”模式，同时保留现有模块能力接口，避免为同一能力增加重复抽象。

本次重构不改变 Controller 路由、请求和响应结构、事务边界、业务规则、数据库访问逻辑及异常语义。现有调用方继续依赖原 `XxxService` 类型，具体实现迁移为 `impl.XxxServiceImpl`。

## 2. 结构约定

普通业务服务采用以下结构：

```text
module/
├─ XxxService.java
└─ impl/
   └─ XxxServiceImpl.java
```

- `XxxService` 只声明公开业务方法，不添加 Spring 注解。
- `XxxServiceImpl` 使用 `@Service`，实现对应接口并承载原有业务逻辑和事务注解。
- Controller、其他 Service 和拦截器继续通过构造器注入 `XxxService` 接口。
- 实现类命名统一使用 `XxxServiceImpl`，不使用 `IXxxService` 命名。
- 原服务中的私有方法保留在实现类，不进入接口。
- 原服务中作为公开契约使用的嵌套 `record` 移入接口，调用方仍通过 `XxxService.RecordName` 引用。

## 3. 改造范围

以下现有业务服务调整为接口和实现类：

```text
SalesAnalyticsService
ProductService
PaymentService
MiniPaymentService
StaffService
TenantService
StoreService
InventoryService
CustomerProfileService
OrderService
CartService
AddressService
OrderPricingService
MiniOrderService
MiniappConfigService
AuthService
MiniAuthService
```

其中 `ProductServiceImpl` 同时实现 `ProductService` 和已有的 `catalog.api.ProductReader`，保证订单、库存等跨模块调用继续依赖更窄的只读契约。

## 4. 已有端口与基础设施

以下类型不再增加同义 Service 接口：

- `InventoryReservationService` 已实现 `inventory.api.InventoryReservation`。实现类迁移并命名为 `InventoryReservationServiceImpl`，调用方继续依赖 `InventoryReservation`。
- `MyBatisPaymentOrderCreator` 已实现支付模块端口，保持现状。

MinIO 存储调整为通用接口：

```text
media/StorageService.java
media/impl/MinioStorageServiceImpl.java
```

业务模块及 Controller 依赖 `StorageService`，MinIO SDK 和配置只保留在实现类。接口暂时保留现有 `upload` 和 `deleteUrl` 两项能力，不扩展未被当前业务使用的方法。

## 5. 兼容性和依赖注入

- 接口沿用原服务类的完整包名，因此大多数调用方无需修改 import。
- Spring 容器中每个接口保持唯一实现，避免注入歧义。
- 实现类使用构造器注入，不新增字段注入。
- `@Transactional` 保留在实现类公开方法上，事务传播行为不变。
- 原来直接实例化具体 Service 的单元测试改为实例化对应 `XxxServiceImpl`；面向依赖的 Mock 继续使用接口。
- 不借本次重构调整业务返回类型，也不顺带重构 Mapper、Entity 或模块包结构。

## 6. 验证方式

按以下顺序验证：

1. 搜索所有旧实现类的构造调用和具体类型引用，确认已迁移。
2. 运行订单和统计相关定向测试，覆盖当前已有的直接实例化场景。
3. 运行 Maven 全量测试，检查 Spring 注入、事务代理和编译兼容性。
4. 运行 `git diff --check`，确认没有空白符错误。
5. 检查 `git status --short` 和差异范围，确保保留任务开始前已有的订单模块修改。

## 7. 验收标准

1. 改造范围内的业务服务均由接口和唯一的 `XxxServiceImpl` 组成。
2. Controller 和服务间依赖使用接口，不直接依赖 `impl` 包。
3. 已有 `ProductReader`、`InventoryReservation` 和支付端口不产生重复接口。
4. 存储调用方依赖 `StorageService`，不再依赖带有 MinIO 技术名称的具体类。
5. 所有现有测试通过，接口重构不改变业务行为。
6. 工作区原有未提交修改完整保留。

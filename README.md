# SoulPermission
 [![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0)
#### Android权限适配的更优解决方案：
 -  解耦Activity和Fragment、不再需要Context、不再需要onPermissionResult
 - 内部涵盖版本判断，一行代码解决权限相关操作，无需在调用业务方写权限适配代码，继而实现真正调用时请求的“真运行时权限”
 - 接入成本低，零入侵，仅需要在gradle配置一行代码
 - 支持多项权限同时请求
 - 支持特殊权限(Notification[通知]、SystemAlert[应用悬浮窗]、未知来源应用安装)的检查与请求
 - 支持系统权限页面跳转
 - 支持debug模式
## Installation：

```java
dependencies {
    implementation 'com.qw:soulpermission:1.1.8'
}

```
[1.1.8 ReleaseNote](https://github.com/soulqw/SoulPermission/releases/tag/1.1.8)
## Usage：

#### 基本用法：
- 一句话版本完成自动判断、权限检查、请求、后续操作：
```java
  SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                //if you want do noting or no need all the callbacks you may use SimplePermissionAdapter instead
                new CheckRequestPermissionListener() {
                    @Override
                    public void onPermissionOk(Permission permission) {
                        Toast.makeText(ApiGuideActivity.this, permission.toString() +
                                "\n is ok , you can do your operations", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(Permission permission) {
                        Toast.makeText(ApiGuideActivity.this, permission.toString() +
                                " \n is refused you can not do next things", Toast.LENGTH_SHORT).show();
                    }
                });

```
- 也可以一次请求多项权限

```java
  SoulPermission.getInstance().checkAndRequestPermissions(
                Permissions.build(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                //if you want do noting or no need all the callbacks you may use SimplePermissionsAdapter instead
                new CheckRequestPermissionsListener() {
                    @Override
                    public void onAllPermissionOk(Permission[] allPermissions) {
                        Toast.makeText(ApiGuideActivity.this, allPermissions.length + "permissions is ok" +
                                " \n  you can do your operations", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(Permission[] refusedPermissions) {
                        Toast.makeText(ApiGuideActivity.this, refusedPermissions[0].toString() +
                                " \n is refused you can not do next things", Toast.LENGTH_SHORT).show();
                    }
                });

```
- 包含shouldShowRequestPermissionRationale的情形

```java
 SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.READ_CONTACTS,
                new CheckRequestPermissionListener() {
                    @Override
                    public void onPermissionOk(Permission permission) {
                        Toast.makeText(ApiGuideActivity.this, permission.toString() +
                                "\n is ok , you can do your operations", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(Permission permission) {
                        // see CheckPermissionWithRationaleAdapter
                        if (permission.shouldRationale()) {
                            Toast.makeText(ApiGuideActivity.this, permission.toString() +
                                    " \n you should show a explain for user then retry ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ApiGuideActivity.this, permission.toString() +
                                    " \n is refused you can not do next things", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

```
- 检查某项权限

```java
//you can also use checkPermissions() for a series of permissions
Permission checkResult = SoulPermission.getInstance().checkSinglePermission(Manifest.permission.ACCESS_FINE_LOCATION);
```
- 检查特殊权限[通知权限]

```java
 boolean checkResult = SoulPermission.getInstance().checkSpecialPermission(Special.NOTIFICATION);
```
- 检查并请求特殊权限[未知应用安装]

```java
 //if you want do noting or no need all the callbacks you may use SimpleSpecialPermissionAdapter instead
    SoulPermission.getInstance().checkAndRequestPermission(Special.UNKNOWN_APP_SOURCES, new SpecialPermissionListener() {
        @Override
        public void onGranted(Special permission) {
                Toast.makeText(ApiGuideActivity.this, "install unKnown app  is enable now", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDenied(Special permission) {
                Toast.makeText(ApiGuideActivity.this, "install unKnown app  is disable yet", Toast.LENGTH_SHORT).show();
        }
    });
```

- 跳转到应用设置页


```java
 SoulPermission.getInstance().goApplicationSettings(new GoAppDetailCallBack() {
            @Override
            public void onBackFromAppDetail(Intent data) {
                //if you need to know when back from app detail
                Utils.showMessage(view, "back from go appDetail");
            }
        });
```

- 设置debug模式(看日志打印)

```java
SoulPermission.setDebug(true);
```

#### 注意事项：
- 最低支持Android 4.0(Api level 14)
- SoulPermission内部自动初始化，如果你项目中使用了通过替换Application方式从而可能会导致SoulPermission内部初始化失败的框架(如Tinker，腾讯乐固等)，请手动在你的Application类中调用init即可（通过设置debug，可以看到错误日志打印和相关Toast）。

```java
//invoke init in your application when auto init failed
public class SimpleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //no necessary
        SoulPermission.init(this);
    }
}

```
- 如果需要在某个页面创建时候请求权限，请在onCreate()中使用、请不要在onResume()调用，否则权限未被动态授予前会陷入死循环。
### Screenshot：
![image](https://img-blog.csdnimg.cn/20190612212049718.png)

![image](https://img-blog.csdnimg.cn/20190530192140891.png)
- for common Permission

![image](https://img-blog.csdnimg.cn/20190530192219180.gif)

- for Special Permission

![image](https://img-blog.csdnimg.cn/2019053019225180.gif)

### MoreDetail：
#### [工作原理和最佳示例](https://blog.csdn.net/u014626094/article/details/89438614)

##### 如果有任何问题或者反馈请联系我：
cd5160866@126.com

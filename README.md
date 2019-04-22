# SoulPermission
#### 一行代码权限的更优解决方案：
 - 实现真正调用时请求的“真运行时权限”
 -  解耦Activity和Fragment、不再需要Context
 - 内部涵盖版本判断，一行代码封装权限请求和后续操作
 - 接入成本低，可以在公共方法中声明以后，无需在调用业务方写权限适配代码
 - 支持多项权限同时请求、支持系统权限页面跳转
## Installation：

```
dependencies {
    implementation 'com.qw:soulpermission:1.0.10'
}
```
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
                        if (permission.shouldRationale) {
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
- 跳转到权限设置


```java
 SoulPermission.getInstance().goPermissionSettings();
```
#### 注意事项：
SoulPermission内部自动初始化，如果你项目中使用了Tinker等可能会导致SoulPermission状态不正确的框架，请手动在你的Application类中调用init即可。

```java
public class SimpleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //no necessary
        SoulPermission.init(this);
    }
}

```
### Screenshot：
![image](https://img-blog.csdnimg.cn/2019042223014322.png)
![image](https://img-blog.csdnimg.cn/20190422230154512.png)

### MoreDetail：
#### [工作原理和最佳示例](https://blog.csdn.net/u014626094/article/details/89438614)

## LICENSE
```
Copyright 2017 Google

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```

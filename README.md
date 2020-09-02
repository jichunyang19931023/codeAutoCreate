
# codeAutoCreate
This plugin can help you create entity-related files.

本插件可以帮助生成和 entity 相关的文件，包括 Respository，Service，ServiceImpl 以及Controller 类。

假设我们的 Entity 类为 TestEntity.java，插件生效的前提条件是，该类必须标注 Entity 注解并且确保这个类在编辑器中是当前正在编辑的类。

**插件编译打包**

点击 Bulid -> Prepare All plugin Modules For Deployment

编译完成后，在工程第一级目录下会生成一个 codeAutoCreate.jar

**安装插件**

点击 File -> Settings -> Plugins 右侧的设置按钮，选择 Install plugin from Disk，选中刚生成的 jar 包并点击确定，此时插件就安装成功了。

**运行插件**

点击 Window 菜单，第一个菜单就是安装好的插件，点击运行即可。

插件运行后会自动生成一个配置文件，默认配置如下（packageName为 entity 的上级目录）：

```sql
# 生成service文件的包
path.service=packageName.service

# 生成serviceImpl文件的包
path.service.impl=packageName.service.impl

# 生成controller文件的包
path.controller=packageName.controller

# 生成repository文件的包
path.repository=packageName.entity.repository
 
```
1.对于 entity 包，暂时只支持命名为 entity 和 domain，实体类所在的目录层次不得大于3。

2.可以对配置文件进行修改，重新 Build Module 后再运行插件即可安装配置生成指定目录结构的文件。

3.如果对应的文件已经存在，为了安全起见，插件会将旧的文件重命名为原文件名.java.old。

本插件默认生成的目录结构如下所示：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200902111339741.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2oxMjMxMjMw,size_16,color_FFFFFF,t_70#pic_center)

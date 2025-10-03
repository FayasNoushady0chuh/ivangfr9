## 分销管理系统

###  引用技术

  guns 权限管理系统

### 适用企业

1、已有项目，需要增加分销功能。

2、项目需要暂时使用分销功能来增加粉丝量，后面可能下线。

3、对接多个系统，需要对多个账户进行控制的系统

### 目标

企业级软件，为企业创造更好的利润

### 项目总览
项目使用场景图示

 ![输入图片说明](https://gitee.com/uploads/images/2018/0609/190905_38816529_1497609.png "分销场景.png")

分销功能架构图

![输入图片说明](https://images.gitee.com/uploads/images/2018/0710/224752_26c20ef5_1497609.jpeg "分销系统总体架构.jpg")

分销内部设计图
![输入图片说明](https://gitee.com/uploads/images/2018/0510/085941_47511fa1_1497609.png "未命名文件.png")

### 项目说明

distribution_management  用来运行分销管理系统

plug用来调试接口，调试完成后，可以直接使用

### 安装方法

下载此项目后，将sql中的 两个数据库初始化到本地数据库，然后根据实际情况更新数据库账户和密码。

运行/distribution_management 目录下的项目,访问http://localhost/。

账号 ： admin /111111

​          dist/123

###  项目背景

在互联网项目兴起的大环境下，定制化分销软件的各种样式层出不穷，但是不管何种分销软件都离不开用户等级、代理、分润的形式，我通过自己的总结，意图将所有分销模式抽象画，在后台可配置化，将微服务的模式融入进来，实现系统的可拔插等。



### 项目特色

####  经典分销模式
如下图中的分销模式，就可以很好的与本系统对接
![输入图片说明](https://gitee.com/uploads/images/2018/0503/203047_afd028a5_1497609.png "未命名文件.png")

#### 强大的账户体系
![输入图片说明](https://gitee.com/uploads/images/2018/0530/161429_4728a749_1497609.png "账户系统 (1).png")

#### 系统可拔插

​      系统以微服务的形式运行，即独立于其他的软件，提供接口进行交互，不会对其他的系统产生新的冗余数据。

​      假设**李老板** 前期需要推广自己的软件，自己的软件又没有设计这个功能，加功能又需要很多钱，这个时候，使用这个软件就可以减少资金的投入（写很少的代码就可以实现自己的需求）。

  系统稳定了，**李老板** 不需要这个分销软件了， 那么可以直接将原来的几行代码关了，或者在自己的系统中增加开关，直接就可以关掉。并且不影响原来的系统的运行。

####  分销配置化

  假设**李老板**  原来只设置一级又分润，后面需要给二级或者三级分润，这个时候只需要在后台进行配置就可以。**李老板** 如果设置原来的配置每笔交易按照百分比收取，后来改为每笔交易按照固定金额收取，就可以直接在后台进行配置。
如下图中的分润配置，可以根据各种情况进行配置分润。
![输入图片说明](https://gitee.com/uploads/images/2018/0616/205340_a24ef103_1497609.jpeg "分润详情.jpg")

#### 会员关系可视化

  会员的发展理论上可以无限制的发展下去，并且可以通过树状图表现出来。

#### 分销关系权限化

每个分销商只能看到自己名下的会员。并且可以看到自己的交易明细，这个就减少原有的系统的开发。

### 平台简介
对接参考，用来对系统进行对接，并且已经写好了的模拟接口，可以模仿使用
![输入图片说明](https://gitee.com/uploads/images/2018/0531/232723_3195cc5d_1497609.jpeg "1.jpg")

会员信息对会员账号和会员资金进行管理
![输入图片说明](https://gitee.com/uploads/images/2018/0531/232930_a2b39ed8_1497609.jpeg "2.jpg")
![输入图片说明](https://gitee.com/uploads/images/2018/0531/233008_65b79616_1497609.jpeg "3.jpg")
![输入图片说明](https://gitee.com/uploads/images/2018/0531/233016_a1f5825b_1497609.jpeg "4.jpg")

分销配置用来配置分润等信息
![输入图片说明](https://gitee.com/uploads/images/2018/0531/233057_0ec35361_1497609.jpeg "5.jpg")
![输入图片说明](https://gitee.com/uploads/images/2018/0531/233120_47801737_1497609.jpeg "6.jpg")
![输入图片说明](https://gitee.com/uploads/images/2018/0531/233110_36d91f83_1497609.jpeg "7.jpg")

交易中心 集中对收入和支出进行管理
![输入图片说明](https://gitee.com/uploads/images/2018/0531/233149_4a1d658e_1497609.jpeg "8.jpg")
![输入图片说明](https://gitee.com/uploads/images/2018/0531/233201_d8fb5542_1497609.jpeg "9.jpg")
统计中心 对数据进行统计
交易动态，可以查看最新的交易成功的动态
![输入图片说明](https://gitee.com/uploads/images/2018/0609/143627_87c48977_1497609.jpeg "111.jpg")
账户收益统计图
![输入图片说明](https://gitee.com/uploads/images/2018/0610/120127_3fc5142b_1497609.jpeg "111.jpg")
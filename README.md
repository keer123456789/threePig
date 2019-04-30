# threePig
# 介绍 
本科毕设是关于区块链溯源的数据管控系统，研究点一共是3个：
1. 实现对BigChainDB实现sql查询。
   这个点做成了桌面可视化工具和网页可视化的工具 桌面可视化工具效果不是很好。
   代码地址：[https://github.com/keer123456789/graduation.git](https://github.com/keer123456789/graduation.git)
2. 使用智能合约实现RBAC模型, 这个点使用web3j，springboot,react做了一个系统工具：
   前端代码 ：[https://github.com/keer123456789/RBAC-react.git](https://github.com/keer123456789/RBAC-react.git)
   后端代码 ：现在是和本库写在了一起，之后会将其分开，…………
3. 使用区块链网关和物联网采集数据，将数据返送给BigChainDB。
   使用树莓派和nodemcu，树莓派当做区块链网关，nodemcu连接采集器，使用nodemcu将数据发送给树莓派，树莓派进行筛选，之后发送给BigChainDB。
   目前树莓派上运行的是springboot的server，nodemcu和springboot之间采用http通信，这个有点笨重，由于毕设时间有限，没有采用mqtt协议，之后
   会用python冲树莓派上的项目。
   目前树莓派上的项目，代码地址: [https://github.com/keer123456789/collection.git](https://github.com/keer123456789/collection.git)

毕设答辩演示的系统将这三个点集成到一起了，应用场景是猪肉溯源，这个是springboot一个后端代码，还有一个react的前端项目，代码地址：[https://github.com/keer123456789/pigReact.git](https://github.com/keer123456789/pigReact.git)
本代码库还有一部分android的后台，android代码是别人的，主要是交易系统在android上，这部分不是我的，就不写地址了。

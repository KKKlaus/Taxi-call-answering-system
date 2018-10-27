# Taxi-call-answering-system
Object-oriented course project

1.测试方法：在TestThread线程中输入类似于：
Passenger passenger = new Passenger("1", Map.getPoint(7, 49), Map.getPoint(45, 71));
passengerQueue.put(passenger);
这样的东西。
稍微解释一下：passenger构造方法第一个字符串是这个乘客的名字，第二个是这个乘客的当前位置，第三个是这个乘客的目的地位置。
我的点的坐标是从1~80，也就是说最左上角的是（1,1），最右下角的是（80,80）
第二个是将请求乘客放入队列中，请使用put方法。

如果想要改变边的连通性，请使用changeEdge方法，方法的前两个参数代表边的两个点，最后一个参数代表边的连通性，true是连通，false是不连通。如果点A和点B不相邻，则会输出wrong edge。指导书中提到说只能关闭或者打开已有的连接边，我这个也支持原来没有的连接边，请不要因为这个扣我分。（不过测试的时候就最好不要测试原来没有的连接边啦，谢谢）

退出请直接用eclipse的terminate，不能自行退出。

2.输出：初始化需要3s左右，完成后会输出initialization over

如果没有出租车能够响应乘客请求，会输出no car available for passenger xxx  xxx代表他的名字；
否则，会输出Taxi No.xx（编号） current location: (xx , xx) current time: x.x credit: x is answering to passenger xxx  类似的话语
如果出租车成功分配到乘客，会输出：Taxi No.xx（编号） current location: (xx , xx) current time: x.x credit: x successfully answered passenger xxx
如果出租车成功接到乘客，会输出：Taxi No.xx current location: (xx , xx) current time: x.x credit: x has picked up passenger xxx
如果出租车成功送完乘客，会输出：Taxi No.xx current location: (xx , xx) current time: x.x credit: x has arrived passenger xx's destination
如果是可追踪的出租车，会显示 Traceable Taxi No.......后面内容同上

3.Map：需要读取的文件是map.txt，存放的路径应该是d:\map.txt，如果你没有d盘或者有其他特殊需求，请修改map类中initial方法fileReader初始化那一行的代码。
关于map文件的内容：一共有80行，每行80个数字，中间没有空格隔开，行与行之间是回车符，示例如下：
23333132312132322322131233231123123333221333323333332233221233332131311313311312
31133231333233233133023233213131333231333232321332333233333311333332222232222332
。。。。
11111111111111111111111111111111111111111111111111111111111111111111111111111110
请注意，地图一定要是连通图，且没有非法数字！

关于道路交叉方式的文件是info.txt，存放的路径应该是d:\info.txt，其他的问题同上。

4.如果地图不是连通图，或者被改变成非连通图了，那么可能会永远无法输出到达目的地的提示。我认为这不算bug，因为不连通的话本来就到不了目的地，所以当然不会输出。

5.所有出租车信息保存在taxiList中，如果想要查看xx号出租车信息请使用getInfo(xx);  1<=xx<=100
注意，我的traceabletaxi编号是1~30，占用数组的71~100。比如你想要获取traceabletaxi No.1的信息请使用getInfo(71);

6.红路灯初始化为所有南北向的为绿灯，所有东西向的为红灯。等红灯的时间不算在等待状态的20s内

7.关于测试每一个方法，有几点需要注意：
直接在Main函数中写测试代码就可以了，同时注意要注释掉其他的代码。请从//comments starts here的下一行开始注释，//comments ends here的上一行结束注释

所有方法中，如果要传入Point的参数，请Map.getPoint(x, y)获取对象。举例如下：比如Taxi的构造方法是： public Taxi(Point p) ，如果你想测试这个参数，那么请使用
new Taxi(Map.getPoint(12, 24)); ，而不是new Taxi(new Point(12, 24, 1));
又比如想测试Point类的setUp方法，那么请使用Map.getPoint(12, 24).setUp(true), 而不是new Point(12, 24, 1).setUp(true)。

有一些方法是private的，这个方法的本意就不是用来给外部调用的，就没有考虑过外部调用的情况，因此这些方法无法进行此次测试。

测试方法是: 首先repOK()测试一下你构造的对象是否正确，在确保满足方法的规格的情况下调用方法，调用完方法后再次调用repOK测试对象是否正确，比如，
taxi.repOK();
taxi.getCredit();
taxi.repOK();

8.关于迭代器的使用方法，说明如下：
首先要确定你想调用的是可追踪的出租车，然后根据编号调用方法getPathInfoByPassenger(int i, Passenger p)；i为编号，p为乘客，若成功，则返回一个双向迭代器，否则会给予失败提示。
迭代器共有4个方法，previous(),hasPrevious(),next(),hasNext()，如果没有前一个元素或者后一个元素而继续调用previous()或next()，会抛出NoSuchElementException，这不算crash，请注意！！！！
具体代码示例可以见testThread中的注释。
如果想要查看访问服务的次数，请使用getServingTimes(int i)方法

9.如果目的地和当前位置相同，以(11 , 32)为例，会输出：
Taxi No.xx current location: (xx , xx) current time: x.x credit: x is answering to passenger xx
Taxi No.xx current location: (11 , 32) current time: x.x credit: x has picked up passenger xx
Taxi No.xx current location: (11 , 32) current time: x.x credit: x has arrived passenger xx's destination
和正常情况一致



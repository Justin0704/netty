# netty
netty - 学习

1、一个EventLoopGroup包含一个或多个EventLoop

2、一个EventLoop在他的生命周期内只和一个Thread绑定

3、所有由EventLoop处理的IO事件都将在它专有的Thread上被处理

4、一个Channel在他的生命周期内只注册于一个EventLoop

5、一个EventLoop可能会被分配给一个或多个Channel

Spring 开发小组意识到在 RMI 服务和基于 HTTP 的服务（如 Hessian 和 Burlap）之间的空
白。一方面， RMI 使用 Java 标准的对象序列化，但很难穿越防火墙；另一方面， Hessian/Burlap
能很好地穿过防火墙工作，但使用自己私有的一套对象序列化机制。
就这样， Spring 的 HttpInvoker 应运而生。 HttpInvoker 是一个新的远程调用模型，作为 Spring
框架的一部分，来执行基于 HTTP 的远程调用（让防火墙高兴的事），并使用 Java 的序列化机
制（这是让程序员高兴的事）。



http invoker 需要在web 容器里气启动，暂时不做研究  
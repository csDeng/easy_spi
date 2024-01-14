# [面试炫技系列~]浅谈 `spi`

> Java SPI（Service Provider Interface）是一种Java的服务提供者接口机制。它允许在运行时动态加载实现服务接口的类。

## 基本概念
`SPI` 机制的基本思想是，定义一个服务接口，多个不同的实现类可以实现这个接口。然后，在`classpath` 路径下的 `META-INF/services` 目录中创建一个以服务接口的全限定名命名的文件，文件内容为实现类的全限定名。这样，当程序运行时，可以通过 `ServiceLoader` 工具类来加载所有实现了该服务接口的类。
使用 `Java SPI` 的好处是，可以在不修改源代码的情况下，通过配置更换实现类，实现程序的可扩展性。而不需要在代码中显式地引用实现类，从而实现了松耦合的设计。
`SPI` 机制在 `Java` 中被广泛应用，例如 `JDBC` 中的 `Driver` 接口和 `Servlet` 容器中的 `Servlet` 接口等。通过 `SPI` 机制，可以在不同的场景中灵活地替换实现类，提供更多的选择和定制化的功能。同时，`SPI` 机制也增加了程序的灵活性和可维护性。
## 最简单的实例
我们现在需要使用一个内容搜索接口，搜索的实现可能是基于文件系统的搜索，也可能是基于数据库的搜索，甚至是 `rpc` 搜索。
> 基本步骤
> 1. 定义接口
> 2. 在 `META-INF/services`目录下，新建接口全限定名（如果是内部接口或内部类需要使用 `$`）的文件，然后在文件里面加上实现类。
> 3. 使用 `ServiceLoader`加载接口，进行使用


```java
public class SpiApplication {

    public interface Search {
        List<String> searchDoc(String keyword);
    }

    public static class FileSearch implements Search{
        @Override
        public List<String> searchDoc(String keyword) {
            System.out.println("文件搜索 "+keyword);
            return null;
        }
    }
    public static class DatabaseSearch implements Search{
        @Override
        public List<String> searchDoc(String keyword) {
            System.out.println("数据搜索 "+keyword);
            return null;
        }
    }

    public static class RpcSearch implements Search{
        @Override
        public List<String> searchDoc(String keyword) {
            System.out.println("rpc搜索 "+keyword);
            return null;
        }
    }


    public static void main(String[] args) {
        ServiceLoader<Search> s = ServiceLoader.load(Search.class);
        Iterator<Search> iterator = s.iterator();
        while (iterator.hasNext()) {
            Search search =  iterator.next();
            search.searchDoc("hello world");
        }
    }
}
```

![](https://img-blog.csdnimg.cn/img_convert/242881044b8b761a24202a0af19568c3.png#id=RHTtC&originHeight=1276&originWidth=2858&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
可以看到输出三行搜索结果，这就是因为 `ServiceLoader.load(Search.class)` 在加载某接口时，会去 `META-INF/services` 下找接口的全限定名文件，再根据里面的内容加载相应的实现类。
这就是 `spi` 的思想，接口的实现由 `provider` 实现，`provider` 只用在提交的 `jar` 包里的 `META-INF/services` 下根据平台定义的接口新建文件，并添加进相应的实现类内容就好。
## 使用 `jar` 包通过 `spi`动态实现接口功能
新增 `jar`包，打包后加载到其他项目运行。
```java
public interface Search {
    List<String> searchDoc(String keyword);
}

//-
public class DefaultSearch implements Search{

    @Override
    public List<String> searchDoc(String keyword) {
        System.out.println("default search");
        return null;
    }
}

//-
public class MainApplicaction {

    public static void main(String[] args) {
        ServiceLoader<Search> s = ServiceLoader.load(Search.class);
        Iterator<Search> iterator = s.iterator();
        while (iterator.hasNext()) {
            Search search =  iterator.next();
            search.searchDoc("hello world");
        }
        System.out.println(">>>> jar main end");
    }


}
```
![](https://img-blog.csdnimg.cn/img_convert/4162761041a295990b2dfef9e85faf54.png#id=nOPPR&originHeight=622&originWidth=860&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)
然后 `mvn install`到本地，把 `jar`包导入到其他项目
![](https://img-blog.csdnimg.cn/img_convert/c90fc38c5abca1e44ce387c3a3ba542f.png#id=Brykw&originHeight=1590&originWidth=2892&originalType=binary&ratio=1&rotation=0&showTitle=false&status=done&style=none&title=)

```java
package com.example.spi;

import com.example.MainApplicaction;
import com.example.Search;

import java.util.List;

public class SpiApplication {



    public static class FileSearch implements Search {
        @Override
        public List<String> searchDoc(String keyword) {
            System.out.println("文件搜索 "+keyword);
            return null;
        }
    }
    public static class DatabaseSearch implements Search{
        @Override
        public List<String> searchDoc(String keyword) {
            System.out.println("数据搜索 "+keyword);
            return null;
        }
    }

    public static class RpcSearch implements Search{
        @Override
        public List<String> searchDoc(String keyword) {
            System.out.println("rpc搜索 "+keyword);
            return null;
        }
    }


    public static void main(String[] args) {
        MainApplicaction.main(new String[0]);
    }
}
```
运行结果
```shell
文件搜索 hello world
数据搜索 hello world
rpc搜索 hello world
default search
>>>> jar main end
```
> 由此可以看到本项目的 `META-INF/services`的加载顺序在 `jar`包前面。


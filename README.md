<h1 align="center" style="color:yellow">HXCin</h1>

一个基于`swing`的Java按键监测, 类似于C/C++的`_getch()`函数. 实现非阻塞的按键输入, 而且不会回显到控制台上. (使用简单、轻量级、非侵入)

> [!TIP]
> 本库主要是为了Java萌新, 在 **控制台** 上输入内容, 可以**不回显**, **不阻塞**. (具体例子可以看 [#2.0 前言: 为什么需要本库?](#20-前言-为什么需要本库))

您只需要把[代码](./src/main/java/com/hx/HXCin.java)拷贝到您的项目中即可使用.

## 一、使用示例

- [阻塞等待输入, 无需回车的输入](./src/test/java/Test01.java)

- [非阻塞输入](./src/test/java/Test01.java)

[简单示例](./src/test/java/Test00.java):

```Java
import com.hx.HXCin; // 导入包

public class Test00 {
    public static void main(String[] args) {
        int cnt = 0;
        HXCin.listenBegin(); // 开始监听 (此时焦点在swing窗口, 您无法执行其他输入在其他窗口)
        while (true) {
            // 暂停
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 每秒打印一次
            if (++cnt == 5) {
                cnt = 0;
                System.out.println("---");
            }

            // 尝试获取字符 (若无字符则返回-1)
            char c = HXCin.tryGetChar();
            if (c != (char)-1) {
                if (c == 'q') {
                    break;
                }
                System.out.println(c);
            }
        }
        HXCin.listenEnd(); // 结束监听 (恢复焦点)
    }
}
```

## 二、API函数
### 2.0 前言: 为什么需要本库?

对于一般的java代码, 输入的时候总是会显示在控制台上:

```sh
请输入名称? Heng_Xin
```

并且需要 **回车**, 才可以接收输入. 则非常的不好.

特别是对于萌新, 在单线程中实现控制台上画面绘制+输入, 会非常难受.

比如`贪吃蛇`这种, 需要主动更新界面的项目.

即便是使用多线程, 也会因为输入有 **回显** 导致布局乱了:

```test
. . . . . . . . . .
. . . . . . . . . .
. * . . . . . . . .
. . . . # # # @ . .
. . . . # . . . . .
. . . . # . . . . .
. . . . . . . . . .
. . . w. . . . . . .
. . . . . . . . . .
```

(比如上面的`w`, 本来是打算向上走的, 却因为输入的**回显**, 导致布局乱了)

而本库, 采用的是使用swing的窗口的按键消息, 来作为输入的字符接收, 因此 **不会** 有回显.

### 2.1 阻塞输入API

```Java
/**
 * @description: 等待键盘输入, 如果没有输入, 则阻塞等待
 * @return: char
 **/
public static char getChar();
```

和普通的输入在代码使用上没有什么区别:

```Java
// 等待输入, 直到有输入, 否则一直阻塞在这里
char c = HXCin.getChar();
System.out.printf("你输入的是: %c\n", c);
```

但是, 在用户体验上, 就是: 它不会把输入的内容显示在控制台上. (除非你自己打印了)

### 2.2 非阻塞输入API

```Java
/**
 * @description: 尝试获取字符, 如果没有输入, 则返回-1 (非阻塞)
 * @return: char, 如果没有则返回 -1
 **/
public static char tryGetChar();
```

特别的, 在调用前, 需要显式调用一次`listenBegin`方法, 以开启监听:

```Java
/**
 * @description: 开启监听键盘事件 (此时焦点会在swing窗口上, 因此你无法在其他窗口输入)
 * 包括 控制台!
 * @return: void
 **/
public static void listenBegin();
```

当你不需要监听的时候, 应该及时调用`listenEnd`方法, 以关闭监听:

```Java
/**
 * @description: 关闭监听键盘事件
 * @author: Heng_Xin
 * @date: 2025/4/13 16:02
 * @return: void
 **/
public static void listenEnd();
```

示例:

```Java
HXCin.listenBegin(); // 开启监听键盘事件 (此时焦点会在swing窗口上, 因此你无法在其他窗口输入)
while (true) {
    char c = HXCin.tryGetChar(); // 尝试获取字符, 如果没有输入, 则返回-1 (非阻塞)
    // todo ...
}
HXCin.listenEnd();  // 关闭监听键盘事件
```

### 2.3 杂项

由于内部维护有字符缓冲区, 有时候用户会输入很多内容, 但是你代码的接收处理速度没有这么快, 这时候就可以使用`clear`方法, 以清空缓冲区, 让之后以玩家最新的输入为准.

```Java
/**
 * @description: 清空缓冲区
 * @return: void
 **/
public static void clear();
```

使用示例:

```Java
HXCin.listenBegin();
// ... 输入了很多东西 ..., 然后停止输入
HXCin.clear(); // 清空输入缓冲区
char c = HXCin.tryGetChar(); // 返回-1, 因为被清空缓冲区了
HXCin.listenEnd();
```

## 3. 开源协议

- 实际上我只有一个要求, 就是 $禁止转载$ 到 CSDN!

> [!TIP]
> 我劝你也是少用, 最好不用CSDN. 这个广告比片网还多的垃圾网站, 里面全是复制粘贴、可信度极低的东西, AI要是联网搜索到CSDN的东西, 包是智商剧降, 错误连篇!
package com.hx;

/*
 * Copyright Heng_Xin. All rights reserved.
 *
 * @Author: Heng_Xin
 * @Date: 2025-04-13 12:01:06
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * */

import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @BelongsProject: HXCin
 * @BelongsPackage: com.hx
 * @Author: Heng_Xin
 * @CreateTime: 2025-04-13  12:01
 * @Description: 一个基于`swing`的Java按键监测, 类似于C/C++的`_getch()`函数.
 *               实现非阻塞的按键输入, 而且不会回显到控制台上. (使用简单、轻量级、非侵入)
 * @Version: 1.0
 */

public class HXCin {
    // 保存唯一实例 (单例)
    private static volatile HXCin instance = null;

    // 私有构造函数, 防止外部创建实例
    private HXCin() {
        JFrame frame = new JFrame();
        frame.setUndecorated(true);          // 不要标题栏
        frame.setSize(1, 1);     // 设置为非常小的尺寸
        frame.setLocationRelativeTo(null);   // 居中
        frame.setAlwaysOnTop(true);          // 保持前置
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 添加键盘事件监听器
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                // 加入队列
                HXCin.instance.buffer.add(e.getKeyChar());
            }
        });

        // 等待窗口创建完成, 并且设置焦点
        SwingUtilities.invokeLater(() -> {
            frame.setFocusable(true); // 允许获得焦点
            frame.setFocusableWindowState(true); // 允许窗口状态改变

            // 设置窗口为可见, 且获取焦点
            frame.setVisible(true);
            frame.setFocusable(true);
            frame.requestFocusInWindow();  // 确保窗口获得焦点

            // 确保焦点不丢失 (坏处是不能输入东西了)
            timer = new Timer(FOCUS_TIME, e -> {
                frame.setVisible(true);
                frame.setFocusable(true);
                frame.requestFocusInWindow();
            });
        });
    }

    // 内部工具方法 执行 makeWindowCin (懒汉式创建/返回)
    private static HXCin make() {
        if (instance == null) {
            synchronized (HXCin.class) {
                if (instance == null) {
                    instance = new HXCin();
                }
            }
        }
        return instance;
    }

    // 全局缓冲区(输入队列)
    private final Queue<Character> buffer = new LinkedList<>();

    // 是否需要监听输入
    private Timer timer = null;

    // 常量, 等待时间, 单位: ms (性能影响)
    private static final int WAIT_TIME = 200;

    // 常量, 获取焦点的时间间隔, 单位: ms (性能影响)
    private static final int FOCUS_TIME = 300;

    /**
     * @description: 等待键盘输入, 如果没有输入, 则阻塞等待
     * @author: Heng_Xin
     * @date: 2025/4/13 15:45
     * @return: char
     **/
    public static char getChar() {
        // 懒汉单例, 创建 makeWindowCin
        HXCin cin = HXCin.make();
        listenBegin();

        // 如果没有则阻塞等待 (学习自 C++ 的 _getch)
        while (cin.buffer.isEmpty()) {
            try {
                Thread.sleep(WAIT_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        listenEnd();
        return cin.buffer.poll();
    }

    /**
     * @description: 开启监听键盘事件 (此时焦点会在swing窗口上, 因此你无法在其他窗口输入)
     * 包括 控制台!
     * @author: Heng_Xin
     * @date: 2025/4/13 16:01
     * @return: void
     **/
    public static void listenBegin() {
        HXCin cin = HXCin.make();
        while (cin.timer == null) {
            try {
                Thread.sleep(WAIT_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        cin.timer.start();
    }

    /**
     * @description: 关闭监听键盘事件
     * @author: Heng_Xin
     * @date: 2025/4/13 16:02
     * @return: void
     **/
    public static void listenEnd() {
        HXCin.make().timer.stop();
    }

    /**
     * @description: 尝试获取字符, 如果没有输入, 则返回-1 (非阻塞)
     * @author: Heng_Xin
     * @date: 2025/4/13 15:45
     * @code:
     * <pre>
     * HXCin.listenBegin(); // 开启监听键盘事件 (此时焦点会在swing窗口上, 因此你无法在其他窗口输入)
     * while (true) {
     *     char c = HXCin.tryGetChar(); // 尝试获取字符, 如果没有输入, 则返回-1 (非阻塞)
     *     // todo ...
     * }
     * HXCin.listenEnd();  // 关闭监听键盘事件
     * </pre>
     * @return: char, 如果没有则返回 -1
     **/
    public static char tryGetChar() {
        HXCin cin = HXCin.make();
        if (cin.buffer.isEmpty()) {
            return (char)-1;
        }
        return cin.buffer.poll();
    }

    /**
     * @description: 清空缓冲区
     * @author: Heng_Xin
     * @date: 2025/4/13 17:18
     * @return: void
     **/
    public static void clear() {
        HXCin.make().buffer.clear();
    }
}

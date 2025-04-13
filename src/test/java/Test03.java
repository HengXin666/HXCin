import com.hx.HXCin;

import java.util.Scanner;

/**
 * @BelongsProject: HXCin
 * @BelongsPackage: PACKAGE_NAME
 * @Author: Heng_Xin
 * @CreateTime: 2025-04-13  18:06
 * @Description: 混合使用示例
 * @Version: 1.0
 */
public class Test03 {
    public static void main(String[] args) {
        System.out.println("请输入名称:");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        while (true) {
            char c = HXCin.getChar();
            if (c == 'q') {
                break;
            }
            System.out.println(name + ": " + c);
        }
        System.out.println("新输入新的名称:");
        name = sc.nextLine();
        System.out.println("你好! " + name);

        // 注意, 因为有窗口的存在, 因此需要手动关闭窗口
        // 或者 使用 System.exit(0); 强制退出程序
        System.exit(0);
        // 否则会等待窗口事件
    }
}
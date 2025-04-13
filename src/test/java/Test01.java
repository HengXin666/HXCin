import com.hx.HXCin;

/**
 * @BelongsProject: HXCin
 * @BelongsPackage: PACKAGE_NAME
 * @Author: Heng_Xin
 * @CreateTime: 2025-04-13  16:53
 * @Description: 阻塞等待输入 (无控制台回显)
 * @Version: 1.0
 */
public class Test01 {
    // @test 阻塞等待输入 (无控制台回显)
    public static void main(String[] args) {
        int[][] grid = {
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };
        int y = 2, x = 2;
        while (true) {
            // 打印
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (i == y && j == x) {
                        System.out.print("人 ");
                    } else {
                        System.out.print("空 ");
                    }
                }
                System.out.println();
            }

            // 移动, 阻塞监听移动事件
            char fx = HXCin.getChar();
            switch (fx) {
                case 'w':
                    if (y > 0) {
                        y--;
                    }
                    break;
                case 'a':
                    if (x > 0) {
                        x--;
                    }
                    break;
                case 's':
                    if (y < grid.length - 1) {
                        y++;
                    }
                    break;
                case 'd':
                    if (x < grid[0].length - 1) {
                        x++;
                    }
                    break;
                default:
                    System.out.println("无效的输入");
                    break;
            }

            // 清屏
            System.out.println(new String(new char[50])
                      .replace("\0", "\r\n"));
        }
    }
}

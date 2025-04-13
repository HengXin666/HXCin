import com.hx.HXCin;

/**
 * @BelongsProject: HXCin
 * @BelongsPackage: PACKAGE_NAME
 * @Author: Heng_Xin
 * @CreateTime: 2025-04-13  16:53
 * @Description: 非阻塞输入
 * @Version: 1.0
 */
public class Test02 {
    // @test 非阻塞输入
    public static void main(String[] args) {
        int[][] grid = {
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };
        int y = 2, x = 2;
        HXCin.listenBegin();
        End: while (true) {
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

            // 移动, 尝试获取输入, 返回-1则无输入
            char fx = HXCin.tryGetChar();
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
                case 'q':
                    break End;
                case (char)-1:
                    break;
                default:
                    System.out.println("无效的输入");
                    break;
            }

            // 等待
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 清屏
            System.out.println(new String(new char[50])
                      .replace("\0", "\r\n"));
        }
        HXCin.listenEnd();
    }
}

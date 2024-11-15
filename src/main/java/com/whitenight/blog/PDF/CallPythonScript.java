package com.whitenight.blog.PDF;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CallPythonScript {
    // 构建Python脚本的相对路径
    private static String pythonScriptPath;
    static {
        try {
            // 在静态代码块中调用希望最先执行的方法
            String projectRoot = new File("").getAbsolutePath();
            pythonScriptPath = new File(projectRoot, "pdf-txt/python/demo_gui/main.py").getCanonicalPath();
            System.out.println("Python Script Path: " + pythonScriptPath);
        } catch (IOException e) {
            System.err.println("静态方法执行出错: " + e.getMessage());
            e.printStackTrace();
            // 可以根据实际情况处理异常
        }
    }
    public static void convertPdfToTxt(String pdfFileName, int startPage, int endPage) {
        try {
            // 使用 ProcessBuilder 执行 Python 脚本
            ProcessBuilder pb = new ProcessBuilder(
                    PdfController.pythonCommand,
                    pythonScriptPath,
                    "--pdfFileName", pdfFileName,
                    "--start", String.valueOf(startPage),
                    "--end", String.valueOf(endPage)
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // 获取 Python 脚本的输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 调用 process.waitFor() 方法时，Java 虚拟机（JVM）会等待该进程结束。
            // 在等待期间，调用该方法的线程将被阻塞，不会执行其他任务。
            int exitCode = process.waitFor();
            //通知主线程任务完成
            PdfController.latch.countDown();
            if (exitCode == 0) {
                System.out.println("Python 脚本执行成功");
            } else {
                System.out.println("Python 脚本执行失败");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

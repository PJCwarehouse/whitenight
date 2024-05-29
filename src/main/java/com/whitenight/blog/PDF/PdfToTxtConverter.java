package com.whitenight.blog.PDF;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class PdfToTxtConverter {
    public static void convertPdfToTxt(String pdfFileName, int startPage, int endPage) {
        try {
            // 设置 Python 脚本的路径
//            String pythonScriptPath = "E:\\Springboot\\idea\\demo\\whitenight\\src\\python\\demo_gui\\main.py";
            // 构建Python脚本的相对路径
            String projectRoot = new File("").getAbsolutePath();
            String pythonScriptPath = new File(projectRoot, "pdf-txt/python/demo_gui/main.py").getCanonicalPath();

            String pythonCommand = getPythonCommand();
            System.out.println("使用python版本为: " + pythonCommand);

            System.out.println("Python Script Path: " + pythonScriptPath);
            // 使用 ProcessBuilder 执行 Python 脚本
            ProcessBuilder pb = new ProcessBuilder(
                    pythonCommand,
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

            // 等待脚本执行完成
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Python 脚本执行成功");
            } else {
                System.out.println("Python 脚本执行失败");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
//    检测操作系统类型，并返回适当的 Python 解释器名称
    private static String getPythonCommand() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "python";  // Windows 使用 "python"
        } else {
            return "python3"; // Linux 和其他系统使用 "python3"
        }
    }
}

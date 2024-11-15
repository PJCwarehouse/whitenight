package com.whitenight.blog.PDF;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManagement {
    private final int INITIAL_POOL_SIZE = Runtime.getRuntime().availableProcessors();
//    private LinkedBlockingQueue
    private ExecutorService executor;
//    ReentrantLock
    public void startThreadPool() {
        int currentCpuLoad = getCurrentCpuLoad();
        int PoolSize = calculatePoolSizeBasedOnCpuLoad(currentCpuLoad);
//        项目的线程池的线程数设计应该随着I/O操作的比例调整
        executor = new ThreadPoolExecutor(PoolSize,PoolSize,1,TimeUnit.HOURS,new LinkedBlockingQueue<>());
    }

    public void shutdown() {
        executor.shutdown();
    }

    public void receivePdfConversionTasks(String pdfFileName, int startPage, int endPage) {
        // 模拟接收任务的过程
        PdfConversionTask task = new PdfConversionTask(pdfFileName, startPage, endPage);
        executor.execute(task);
    }

    private int getCurrentCpuLoad() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        return (int) (osBean.getSystemLoadAverage() * 100);
    }

    private int calculatePoolSizeBasedOnCpuLoad(int cpuLoad) {
        // 根据CPU负载计算线程池大小的逻辑
        // 例如，CPU负载低于50%时，可以使用更多的线程
        return Math.min(INITIAL_POOL_SIZE * 2, (cpuLoad < 50 ? 8 : 4));
    }

//    PdfConversionTask 显然是与 PdfConverter 相关的，因此将其作为内部类放置在同一个文件中是有意义的。
//    想要将 PdfConversionTask 的逻辑直接放入 PdfConverter 类中，可以这样做，但这通常不是一个好的设计选择，因为它会违反单一职责原则。
//    单一职责原则指出一个类应该只有一个原因引起它的变更，即一个类应该只负责一个功能。
    private static class PdfConversionTask implements Runnable {
        private final String pdfFileName;
        private int startPage;
        private int endPage;

        public PdfConversionTask(String pdfFileName, int startPage, int endPage) {
            this.pdfFileName = pdfFileName;
            this.startPage = startPage;
            this.endPage = endPage;
        }

        @Override
        public void run() {
            int pageLength = endPage - startPage;
            while(pageLength > 40){
                CallPythonScript.convertPdfToTxt(pdfFileName, startPage, startPage + 40);
                startPage += 40;
                pageLength -= 40;
                System.out.println("执行起始页数：" + startPage);
            }
            // 调用PDF转换函数
            CallPythonScript.convertPdfToTxt(pdfFileName, startPage, endPage);
        }
    }
}
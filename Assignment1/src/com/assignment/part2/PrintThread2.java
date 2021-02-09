package com.assignment.part2;

public class PrintThread2 extends Thread {
    private String text = "";
    private final Control control;
    private final int n_max = 500;

    public PrintThread2(String text, Control control) {
        this.text = text;
        this.control = control;
    }

    @Override
    public void run() {
        synchronized (control) {
            for (int i = 0; i < n_max; i++) {
                while (control.flag && this.text.equals("-")) {
                    try {
                        control.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                while (!control.flag && this.text.equals("|")) {
                    try {
                        control.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.print(this.text);
                control.flag = !control.flag;
                control.notifyAll();
            }
        }
    }
}

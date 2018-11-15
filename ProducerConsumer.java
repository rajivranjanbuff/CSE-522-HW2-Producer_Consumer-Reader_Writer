
// HW2:  Outline of Producer-Consumer:

import java.util.Random;

public class ProducerConsumer {
	public static void main(String[] args) {
		DropBox db = new DropBox(5);
		Producer p = new Producer(db);
		Consumer c = new Consumer(db);
		p.start();
		c.start();
	}
}

class Producer extends Thread {
	private DropBox db;

	public Producer(DropBox db) {
		this.db = db;
	}

	public void run() {
		for (int i = 0; i < 20; i++) {
			db.put(i);
			try {
				Thread.sleep(new Random().nextInt(100));
			} catch (Exception e) {
			}

		}
	}
}

class Consumer extends Thread {
	private DropBox db;
	int value;

	public Consumer(DropBox db) {
		this.db = db;
	}

	public void run() {
		while (true) {
			value = db.get();
			try {
				Thread.sleep(new Random().nextInt(500));
			} catch (Exception e) {
			}

		}
	}
}

class DropBox {

	private int data[];
	private int p;
	private int g;
	int count;
	int value;

	public DropBox(int n) {
		data = new int[n];
		p = 0;
		g = 0;
		count = 0;
	}

	public boolean empty() {
		if (count == 0)
			return true;
		return false;
	}

	public boolean full() {
		if (count == data.length)
			return true;
		return false;
	}

	public synchronized int get() {
		int ans = -1;

		while (empty()) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		ans = data[g];
		g = (g + 1) % data.length;
		count--;
		notify();
		System.out.println(" Get " + ans);
		return ans;
	}

	public synchronized void put(int v) {

		while (full()) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		p = g + count;
		data[p % data.length] = v;
		System.out.println(" Put " + v);
		count++;
		notify();

	}

}

package computer_network.projects.IM.logging;

public class Logger {

	private String name;
	private String prefix = "";

	public Logger(String name) {
		this.name = name;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix + " ";
	}
	
	public void fine(String s) {
	}

	public void info(String msg) {
		System.out.println(name + "\t" + prefix + msg);
	}

	public void error(String msg) {
		System.err.println(name + "\t" + prefix + msg);
	}
	
	public void unhandledException(Exception exception) {
		System.err.println(name + "\t" + prefix + "未处理异常：" + exception);
		exception.printStackTrace();
	}

}

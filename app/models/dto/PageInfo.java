package models.dto;

public class PageInfo {

	private int currentPage;
	
	private int start;
	
	private int end;
	
	private int total;
	
	public PageInfo(int currentPage, int start, int end, int total){
		this.currentPage = currentPage;
		this.start =start;
		this.end = end;
		this.total = total;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	
}

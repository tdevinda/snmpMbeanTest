package lk.dialog;

public class TrapManage implements TrapManageMBean {

	private Integer status = 0;
	
	public TrapManage() {
	}

	@Override
	public Integer getStatus() {
		return status;
	}

	@Override
	public void setStatus(Integer i) {
		this.status = i;
	}

}

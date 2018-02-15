package metier;

import java.util.UUID;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class BankAccount {
	
	private @Id String id;
	private String name;
	private String lastname;
	private Float account;
	private String risk;
	
	public BankAccount() {}

	public BankAccount(String name, String lastname, Float account, String risk) {
		super();
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.lastname = lastname;
		this.account = account;
		this.risk = risk;
	}
	
	public BankAccount(String name, String lastname, Float account) {
		super();
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.lastname = lastname;
		this.account = account;
		this.risk = "low";
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Float getAccount() {
		return account;
	}

	public void setAccount(Float account) {
		this.account = account;
	}
	
	public void addAccount(Float ammount) {
		this.account += ammount;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	@Override
	public String toString() {
		return "BankAccount [id=" + id + ", name=" + name + ", lastname=" + lastname + ", account=" + account
				+ ", risk=" + risk + "]";
	}
}

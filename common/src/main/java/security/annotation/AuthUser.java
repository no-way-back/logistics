package security.annotation;

import java.util.UUID;

public record AuthUser(
	UUID userId,
	String username,
	UserRole role
) {
	public boolean isMaster() {
		return role == UserRole.MASTER;
	}

	public boolean isHubManager() {
		return role == UserRole.HUB_MANAGER;
	}

	public boolean isDeliveryManager() {
		return role == UserRole.DELIVERY_MANAGER;
	}

	public boolean isCompanyManager() {
		return role == UserRole.COMPANY_MANAGER;
	}
}

package org.magic.api.interfaces;

import java.io.IOException;
import java.util.List;

import org.magic.api.beans.OrderEntry;

public interface MTGShopper extends MTGPlugin {

	public List<OrderEntry> listOrders() throws IOException;

}

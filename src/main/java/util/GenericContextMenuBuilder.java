package util;

import java.util.List;
import java.util.function.Function;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;

public class GenericContextMenuBuilder {
	public static <T> void attach(
			TableView<T> tableView,
			Function<T, List<MenuItem>> menuItemSupplier
	){
		tableView.setRowFactory(tv -> {
			TableRow<T> row = new TableRow<T>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getButton() == MouseButton.SECONDARY) {
					ContextMenu contextMenu = new ContextMenu();
					List<MenuItem> items = menuItemSupplier.apply(row.getItem());
					contextMenu.getItems().addAll(items);
					contextMenu.show(row, event.getScreenX(), event.getScreenY());
				}
			});
			return row;
		});
	}
}

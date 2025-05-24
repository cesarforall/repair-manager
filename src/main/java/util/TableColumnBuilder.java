package util;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableColumnBuilder {
	public static <S, T> void addColumn(
			TableView<S> tableView,
			String title,
			String property,
			int width
	) {
		TableColumn<S, T> column = new TableColumn<>(title);
		column.setCellValueFactory(new PropertyValueFactory<S, T>(property));
		column.setPrefWidth(width);
		tableView.getColumns().add(column);
	}
}

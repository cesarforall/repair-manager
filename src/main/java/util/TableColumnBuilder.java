package util;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class TableColumnBuilder {
	public static <S, T> void addColumn(
			TableView<S> tableView,
			String title,
			int width,
			Callback<CellDataFeatures<S, T>, ObservableValue<T>> cellFactory
	) {
		TableColumn<S, T> column = new TableColumn<>(title);
		column.setCellValueFactory(cellFactory);
		column.setPrefWidth(width);
		tableView.getColumns().add(column);
	}
}

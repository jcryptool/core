package algorithmstool.ui;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class TableShellExample {

  Display d;

  Shell s;

  TableShellExample() {
    d = new Display();
    s = new Shell(d);

    s.setSize(250, 200);
    
    s.setText("A Table Shell Example");
    s.setLayout(new FillLayout());

    Table t = new Table(s, SWT.BORDER);

    TableColumn tc1 = new TableColumn(t, SWT.CENTER);
    TableColumn tc2 = new TableColumn(t, SWT.CENTER);
    TableColumn tc3 = new TableColumn(t, SWT.CENTER);
    tc1.setText("First Name");
    tc2.setText("Last Name");
    tc3.setText("Address");
    tc1.setWidth(70);
    tc2.setWidth(70);
    tc3.setWidth(80);
    t.setHeaderVisible(true);

    TableItem item1 = new TableItem(t, SWT.NONE);
    item1.setText(new String[] { "Tim", "Hatton", "Kentucky" });
    TableItem item2 = new TableItem(t, SWT.NONE);
    item2.setText(new String[] { "Caitlyn", "Warner", "Ohio" });
    TableItem item3 = new TableItem(t, SWT.NONE);
    item3.setText(new String[] { "Reese", "Miller", "Ohio" });

    s.open();
    while (!s.isDisposed()) {
      if (!d.readAndDispatch())
        d.sleep();
    }
    d.dispose();
  }

  public static void main(String[] argv) {
    new TableShellExample();
  }

}

          
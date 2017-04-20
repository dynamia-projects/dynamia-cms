package tools.dynamia.cms.admin.importer;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.zkoss.util.media.Media;
import org.zkoss.zul.Fileupload;

import tools.dynamia.actions.ActionRenderer;
import tools.dynamia.cms.admin.importer.ui.Importer;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.integration.Containers;
import tools.dynamia.integration.ProgressMonitor;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer;

public abstract class ImportExcelAction<T> extends ImportAction {

	private List<T> data;

	public ImportExcelAction() {
		setName("Import");
		setImage("export-xlsx");
		setAttribute("background", "#5cb85c");
		setAttribute("color", "white");
	}

	public ImportExcelAction(String name) {
		this();
		setName(name);
	}

	@Override
	public void actionPerformed(Importer win) {
		Fileupload.get(event -> {
			final Media media = event.getMedia();
			if (media != null) {
				String format = media.getFormat();

				if (format.endsWith("xls") || format.endsWith("xlsx")) {
					try {
						data = importFromExcel(media.getStreamData(), getMonitor());
						win.initTable(data);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					UIMessages.showMessage("El archivo debe ser en formato excel", MessageType.ERROR);
				}

			} else {
				UIMessages.showMessage("Seleccione archivo de excel para importar", MessageType.ERROR);
			}
		});
	}

	public abstract List<T> importFromExcel(InputStream excelFile, ProgressMonitor monitor) throws Exception;

	@Override
	public void processImportedData(Importer importer) {
		CrudService crudService = Containers.get().findObject(CrudService.class);

		crudService.executeWithinTransaction(() -> {
			getData().forEach(d -> crudService.save(d));
		});
		UIMessages.showMessage("Import OK");
		importer.clearTable();
	}

	public List<T> getData() {
		if (data == null) {
			data = Collections.EMPTY_LIST;
		}
		return data;
	}

	@Override
	public ActionRenderer getRenderer() {
		return new ToolbarbuttonActionRenderer(true);
	}

}

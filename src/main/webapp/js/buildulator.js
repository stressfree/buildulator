$(document).ready(function() {
    $('#materialsList').dataTable({
    	"bProcessing": true,
    	"bJQueryUI": true,
		"sPaginationType": "full_numbers",
		"bLengthChange": false,
		"iDisplayLength": 50,
    	"sAjaxSource": './library/materials/list.json',
    	"aoColumnDefs": [
    	              { "sClass": "column-1", "aTargets": [0] },
    	              { "sClass": "column-2", "aTargets": [1] },
    	              { "sClass": "column-3", "aTargets": [2] },
    	              { "sClass": "column-4", "aTargets": [3] },
    	              { "sClass": "column-5", "aTargets": [4] },
    	              { "sClass": "column-6", "aTargets": [5] }
    	             ]
    }).makeEditable({
        sAddURL: "./library/materials",
        sDeleteURL: "./library/materials/delete",
        sUpdateURL: "./library/materials/update",
        aoColumns: [
                      { cssclass: "required" },
                      { cssclass: "required" },
                      { cssclass: "required number" },
                      { cssclass: "required number" },
                      { cssclass: "required number" },
                      { cssclass: "required number" }
                  ]
    });

    $('#formBulkAddRows').dialog({ autoOpen: false });
    $('#btnBulkAddRows').removeAttr("disabled").click(function() {
    	$('#formBulkAddRows').dialog('open')
    });
});


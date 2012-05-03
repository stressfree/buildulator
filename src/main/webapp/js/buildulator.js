$(document).ready(function() {
    if ($('#flashMessage p.flashMessageContent').html() != null) {
        $.gritter.add({
            title: $('#flashMessage p.flashMessageTitle').html(),
            text: $('#flashMessage p.flashMessageContent').html()
        });
    }
});

$(document).ready(function() {

    $('#btnEditProject').click(function() {
        document.location.href = editProjectUrl;
    });

    $('#formCloneProject').dialog({
        autoOpen : false,
        modal : true
    });

    $('#btnCloneProject').click(function() {
        $('#formCloneProject').dialog('open');
    });

    $('#formCloneProject .cancelAction a').click(function() {
        $('#formCloneProject').dialog('close');
    });

    $('#formDeleteProject').dialog({
        autoOpen : false,
        modal : true
    });

    $('#btnDeleteProject').click(function() {
        $('#formDeleteProject').dialog('open');
    });

    $('#formDeleteProject .cancelAction a').click(function() {
        $('#formDeleteProject').dialog('close');
    });
});

$(document).ready(function() {
    $('#libraryTabs').tabs();

    $('#materialsList').dataTable({
        "bProcessing" : true,
        "bJQueryUI" : true,
        "sPaginationType" : "full_numbers",
        "bLengthChange" : false,
        "iDisplayLength" : 50,
        "sAjaxSource" : './library/materials/list.json',
        "aoColumnDefs" : [ {
            "sClass" : "column-1",
            "aTargets" : [ 0 ]
        }, {
            "sClass" : "column-2",
            "aTargets" : [ 1 ]
        }, {
            "sClass" : "column-3",
            "aTargets" : [ 2 ]
        }, {
            "sClass" : "column-4",
            "aTargets" : [ 3 ]
        }, {
            "sClass" : "column-5",
            "aTargets" : [ 4 ]
        }, {
            "sClass" : "column-6",
            "aTargets" : [ 5 ]
        } ]
    }).makeEditable({
        sAddURL : "./library/materials",
        sDeleteURL : "./library/materials/delete",
        sUpdateURL : "./library/materials/update",
        sAddNewRowButtonId: "btnAddMaterial",
        sAddNewRowFormId: "formAddMaterial",
        sDeleteRowButtonId: "btnDeleteMaterial",
        oDeleteRowButtonOptions : {},
        aoColumns : [ {
            cssclass : "required"
        }, {
            cssclass : "required"
        }, {
            cssclass : "required number"
        }, {
            cssclass : "required number"
        }, {
            cssclass : "required number"
        }, {
            cssclass : "required number"
        } ]
    });

    $('#formBulkAddMaterials').dialog({
        autoOpen : false,
        modal : true
    });
    $('#btnBulkAddMaterials').removeAttr("disabled").click(function() {
        $('#formBulkAddMaterials').dialog('open')
    });


    $('#energySourcesList').dataTable({
        "bProcessing" : true,
        "bJQueryUI" : true,
        "sPaginationType" : "full_numbers",
        "bLengthChange" : false,
        "iDisplayLength" : 50,
        "sAjaxSource" : './library/energysources/list.json',
        "aoColumnDefs" : [ {
            "sClass" : "column-1",
            "aTargets" : [ 0 ]
        }, {
            "sClass" : "column-2",
            "aTargets" : [ 1 ]
        }, {
            "sClass" : "column-3",
            "aTargets" : [ 2 ]
        } ]
    }).makeEditable({
        sAddURL : "./library/energysources",
        sDeleteURL : "./library/energysources/delete",
        sUpdateURL : "./library/energysources/update",
        sAddNewRowButtonId: "btnAddEnergySource",
        sAddNewRowFormId: "formAddEnergySource",
        sDeleteRowButtonId: "btnDeleteEnergySource",
        oDeleteRowButtonOptions : {},
        aoColumns : [ {
            cssclass : "required"
        }, {
            cssclass : "required number"
        }, {
            cssclass : "required number"
        } ]
    });
});

$(document).ready(function() {
    $('#projectsList').dataTable({
        "bProcessing" : true,
        "bJQueryUI" : true,
        "sPaginationType" : "full_numbers",
        "bLengthChange" : false,
        "iDisplayLength" : 50,
        "sAjaxSource" : './projects/list.json',
        "fnDrawCallback": function(){
            $('.dataTable tbody tr').click(function() {
                var projectId = $(this).attr('id');
                document.location.href = showProjectUrl + projectId;
            });
            $('.dataTable tbody tr').hover(function() {
                $(this).addClass('row_selected');
            }, function() {
                $(this).removeClass('row_selected');
            });
        },
        "aoColumns": [ {
            "bSortable": true
        }, {
            "bVisible": false
        }, {
            "bSortable": true
        }, {
            "iDataSort": 4
        }, {
            "bVisible": false
        } ],
        "aoColumnDefs" : [ {
            "fnRender": function ( oObj, sVal ) {
                var name = '<strong>' + sVal + '</strong>';
                var address = oObj.aData[1];
                if (address != '') {
                    name += '<br/>' + address;
                }
                return name;
            },
            "sClass" : "column-1",
            "aTargets" : [ 0 ]
        }, {
            "sClass" : "column-2",
            "aTargets" : [ 2 ]
        }, {
            "sClass" : "column-3",
            "aTargets" : [ 3 ]
        } ]
    });
});

$(document).ready(function() {
    $('#adminTabs').tabs();
    $('#adminAccordion').accordion({autoHeight: false});

    $('#usersList').dataTable({
        "bProcessing" : true,
        "bJQueryUI" : true,
        "sPaginationType" : "full_numbers",
        "bLengthChange" : false,
        "iDisplayLength" : 50,
        "sAjaxSource" : './admin/users/list.json',
        "aoColumnDefs" : [ {
            "sClass" : "column-1",
            "aTargets" : [ 0 ]
        }, {
            "sClass" : "column-2",
            "aTargets" : [ 1 ]
        }, {
            "sClass" : "column-3",
            "aTargets" : [ 2 ]
        }, {
            "sClass" : "column-4",
            "aTargets" : [ 3 ]
        }, {
            "sClass" : "column-5",
            "aTargets" : [ 4 ]
        }]
    }).makeEditable({
        sDeleteURL : "./admin/users/delete",
        sUpdateURL : "./admin/users/update",
        oDeleteRowButtonOptions : {},
        aoColumns : [ {
            cssclass : "required"
        }, {
            cssclass : "required"
        }, {
            cssclass : "required"
        }, {
            cssclass : "required",
            type: "select",
            onblur: "submit",
            loadurl: "./admin/users/roles.json",
            loadtype: "GET",
            event: "click"
        }, {
            cssclass : "required",
            type: "select",
            onblur: "submit",
            loadurl: "./admin/users/statuses.json",
            loadtype: "GET",
            event: "click"
        } ]
    });
});

$(document).ready(function() {
    $('button, input:submit').button();
});

$(document).ready(function() {
    $("form").validate();
});

$(document).ready(function() {
    $('textarea.rteditor').wysiwyg({
        css: wysiwygCss,
        iFrameClass : "wysiwyg-input",
        controls : {
            paragraph : {
                visible : true,
                groupIndex : 0
            },
            h1 : {
                visible : false
            },
            h2 : {
                visible : true,
                groupIndex : 0
            },
            h3 : {
                visible : true,
                groupIndex : 0
            },
            h4 : {
                visible : true,
                groupIndex : 0
            },
            bold : {
                visible : true,
                groupIndex : 1
            },
            italic : {
                visible : true,
                groupIndex : 1
            },
            underline : {
                visible : false
            },
            strikeThrough : {
                visible : false
            },
            justifyLeft : {
                visible : false
            },
            justifyRight : {
                visible : false
            },
            justifyCenter : {
                visible : false
            },
            justifyFull : {
                visible : false
            },
            insertImage : {
                visible : false
            },
            insertTable : {
                visible : false
            },
            code : {
                visible : false
            },
            html : {
                visible : true
            },
        }
    });
});

$(document).ready(function() {
    $('select').selectmenu();
});

$(document).ready(function() {
    $('#errorAccordion').accordion({autoHeight: false});
});
$(document).ready(function() {
    if ($('#flashMessage p.flashMessageContent').html() != null) {
        $.gritter.add({
            title: $('#flashMessage p.flashMessageTitle').html(),
            text: $('#flashMessage p.flashMessageContent').html()
        });
    }
});

$(document).ready(function() {
    $('div.newProject button').click(function() {
        document.location.href = config.projectsNewUrl;
    });

    $('#btnEditProject').click(function() {
        document.location.href = config.projectsEditUrl;
    });

    $('#formCloneProject').dialog({
        autoOpen : false,
        modal : true,
        resizable: false
    });

    $('#btnCloneProject').click(function() {
        $('#formCloneProject').dialog('open');
    });

    $('#formCloneProject .cancelAction a').click(function() {
        $('#formCloneProject').dialog('close');
    });

    $('#formDeleteProject').dialog({
        autoOpen : false,
        modal : true,
        resizable: false
    });

    $('#btnDeleteProject').click(function() {
        $('#formDeleteProject').dialog('open');
    });

    $('#formDeleteProject .cancelAction a').click(function() {
        $('#formDeleteProject').dialog('close');
    });
});

$(document).ready(function() {
    $('#materialsList').dataTable({
        'bProcessing' : true,
        'bJQueryUI' : true,
        'sPaginationType' : 'full_numbers',
        'bLengthChange' : false,
        'iDisplayLength' : 50,
        'sAjaxSource' : config.materialLibraryUrl + '/list.json',
        'aoColumnDefs' : [ {
            'sClass' : 'column-1',
            'aTargets' : [ 0 ]
        }, {
            'sClass' : 'column-2',
            'aTargets' : [ 1 ]
        }, {
            'sClass' : 'column-3',
            'aTargets' : [ 2 ]
        }, {
            'sClass' : 'column-4',
            'aTargets' : [ 3 ]
        }, {
            'sClass' : 'column-5',
            'aTargets' : [ 4 ]
        }, {
            'sClass' : 'column-6',
            'aTargets' : [ 5 ]
        }, {
            'sClass' : 'column-7',
            'aTargets' : [ 6 ]
        } ]
    }).makeEditable({
        sAddURL : config.materialLibraryUrl,
        sDeleteURL : config.materialLibraryUrl + '/delete',
        sUpdateURL : config.materialLibraryUrl + '/update',
        sAddNewRowButtonId: 'btnAddMaterial',
        sAddNewRowFormId: 'formAddMaterial',
        oAddNewRowOkButtonOptions: {
            'label' : config.saveText
        },
        sDeleteRowButtonId: 'btnDeleteMaterial',
        oDeleteRowButtonOptions : {},
        aoColumns : [ {
            cssclass : 'required'
        }, {
            cssclass : 'required',
            type: 'select',
            onblur: 'submit',
            loadurl: config.materialLibraryUrl + '/types.json',
            loadtype: 'GET',
            event: 'click'
        }, {
            cssclass : 'required'
        }, {
            cssclass : 'number'
        }, {
            cssclass : 'required number'
        }, {
            cssclass : 'required number'
        }, {
            cssclass : 'number'
        } ]
    });

    $('#formBulkAddMaterials').dialog({
        autoOpen : false,
        modal : true,
        resizable: false
    });
    $('#btnBulkAddMaterials').removeAttr("disabled").click(function() {
        $('#formBulkAddMaterials').dialog('open');
    });
});

$(document).ready(function() {
    var url = config.projectsUrl + 'list.json';

    if (config.projectsListAll == 'true') {
        url += '?all=true';
    }

    $('#projectsList').dataTable({
        "bProcessing" : true,
        "bJQueryUI" : true,
        "sPaginationType" : "full_numbers",
        "bLengthChange" : false,
        "iDisplayLength" : 50,
        "sAjaxSource" : url,
        "fnDrawCallback": function(){
            $('.dataTable tbody tr').click(function() {
                var projectId = $(this).attr('id');
                document.location.href = config.projectsUrl + projectId;
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
        }, {
            "bVisible": false
        }, {
            "bVisible": false
        } ],
        "aoColumnDefs" : [ {
            "fnRender": function ( oObj, sVal ) {
                var name = '<strong>' + sVal + '</strong>';
                var address = oObj.aData[1];
                var template = oObj.aData[6];
                var comparable = oObj.aData[7];

                if (template || comparable) {
                    name += ' <em>(';
                }
                if (template) {
                    name += 'Template';
                }
                if (template && comparable) {
                    name += ' &amp; ';
                }
                if (comparable) {
                    name += 'Comparable';
                }
                if (template || comparable) {
                    name += ')</em>';
                }

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
            "fnRender": function ( oObj, sVal ) {
                var created = sVal;
                if (config.projectsListAll == 'true') {
                    created += '<br/><em>' + oObj.aData[5] + '</em>';
                }
                return created;
            },
            "sClass" : "column-3",
            "aTargets" : [ 3 ]
        } ]
    });

    $('#formAddMaterial select').selectmenu({width: '250px'});
});

$(document).ready(function() {
    $('div.bomInstructions').accordion({active: 2, collapsible: true, autoHeight: false});

    $('#adminTabs').tabs();
    $('#adminAccordion').accordion({autoHeight: false});

    $('#usersList').dataTable({
        "bProcessing" : true,
        "bJQueryUI" : true,
        "sPaginationType" : "full_numbers",
        "bLengthChange" : false,
        "iDisplayLength" : 50,
        "sAjaxSource" : config.adminUsersUrl + '/list.json',
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
        sDeleteURL : config.adminUsersUrl + "/delete",
        sUpdateURL : config.adminUsersUrl + "/update",
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
            loadurl: config.adminUsersUrl + "/roles.json",
            loadtype: "GET",
            event: "click"
        }, {
            cssclass : "required",
            type: "select",
            onblur: "submit",
            loadurl: config.adminUsersUrl + "/statuses.json",
            loadtype: "GET",
            event: "click"
        } ]
    });
});

$(document).ready(function() {
    $('#body button, #body input:submit, div.actionButtons button,  div.actionButtons input:submit').button();

    $('div.userWarning a').click(function(e) {
        e.preventDefault();
        $('#googleSignInForm').submit();

    });
    $('#usermenu button').hover(function() {
        $(this).addClass('hoverButton');
    }, function() {
        $(this).removeClass('hoverButton');
    });

    $("form").validate();
});

$(document).ready(function() {
    $('#viewAllProjects').change(function() {
        alert($(this).val());
        $('#viewAllProjectsForm').submit();
    });
});

$(document).ready(function() {
    $('textarea.rteditor').wysiwyg({
        css: config.wysiwygCss,
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
    $('#project select').selectmenu({width: '320px'});
    $('#preferences select').selectmenu({width: '320px'});
});

$(document).ready(function() {
    $('#errorAccordion').accordion({autoHeight: false});
});

$(document).ready(function() {

    $('#formImportBOM').dialog({
        autoOpen : false,
        modal : true,
        resizable: false
    });

    $('#bomImportButton').click(function() {
        $('#formImportBOM').dialog('open');
    });

    $('#bomSkipImportButton').click(function() {
        $('.bomEmpty').removeClass('bomEmpty');
        $('div.bomImport').remove();
    });
});


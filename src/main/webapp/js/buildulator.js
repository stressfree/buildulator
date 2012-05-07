var bomData;

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
        modal : true,
        resizable: false
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

    $('div.billOfMaterials').each(function () {
        var pid = this.id.replace('billOfMaterials','');
        var bom = new BillOfMaterials({projectId: pid, projectUrl: showProjectUrl});
        bom.render();
     });
 });


function BillOfMaterials (config) {
    if (config == undefined) { config = new Array(); }
    var _data;
    var _div = (config.div != undefined) ? config.div : 'div.billOfMaterials div.content';
    var _projectId = (config.projectId != undefined) ? config.projectId : 1;
    var _projectUrl = (config.projectUrl != undefined) ? config.projectUrl : './projects/';
    var _addText = (config.addText != undefined) ? config.addText : 'Add';
    var _editText = (config.editText != undefined) ? config.editText : 'Update';
    var _cancelText = (config.cancelText != undefined) ? config.cancelText : 'Cancel';
    var _addSectionText = (config.sectionText != undefined) ? config.sectionText : 'Add section';
    var _addElementText = (config.elementText != undefined) ? config.materialText : 'Add element';
    var _addMaterialText = (config.materialText != undefined) ? config.materialText : 'Add material';
    var _deleteText = (config.deleteText != undefined) ? config.deleteText : 'Delete';
    var _deleteConfirmText = (config.deleteConfirmText != undefined) ? config.deleteConfirmText : 'Are you sure you want to delete this';
    var _headerNameText = (config.headerNameText != undefined) ? config.headerNameText : '';
    var _headerQuantityText = (config.headerQuantityText != undefined) ? config.headerQuantityText : 'Quantity';
    var _headerEnergyText = (config.headerEnergyText != undefined) ? config.headerEnergyText : 'Energy';
    var _headerCarbonText = (config.headerCarbonText != undefined) ? config.headerCarbonText : 'Carbon';
    var _footerSummaryText = (config.footerSummaryText != undefined) ? config.footerSummaryText : 'Total material energy/carbon footprint';

    this._render = function() {
        var bomUrl = _projectUrl + _projectId + '/bom.json';

        $.getJSON(bomUrl, function(data){
            _data = data;

            var sections = [];
            $(_data.sections).each(function(index, section){
                sections[index] = _renderSection(section);
            });

            $('<div/>', {
                'class': 'bomHeader', html: _renderHeader()
            }).appendTo(_div);

            $('<ul/>', {
                'class': 'bomSections', html: sections.join('')
                + _renderAddControl('section')
            }).appendTo(_div);

            $('<div/>', {
                'class': 'bomFooter', html: _renderFooter()
            }).appendTo(_div);

            _addDefaultHandlers();
        });
    };

    function _addDefaultHandlers() {
        $(_div + ' div.deleteFromBOM a').click(function () {
            _confirmDelete(this);
        });
        $(_div + ' li.addToBOM a').click(function () {
            _displayAddForm(this);
        });
        $(_div + ' div.bomEditable').dblclick(function () {
            _displayEditForm(this);
        });
    }

    function _assignAddSubmitCancelOptionEventHandlers(addDiv) {
        var type = _getType(addDiv);

        $(addDiv).find('input.bomSubmit').click(function(e) {
            e.preventDefault();
            $(this).closest('form').submit();
        });

        $(addDiv).find('input.bomCancel').click(function(e) {
            e.preventDefault();
            addDiv.html(_renderAddControlText(type));
            $(addDiv).find('a').click(function() {
                _displayAddForm(this);
            });
        });

        var form = $(addDiv).find('form');
        form.validate();
        form.submit(function (e) {
            e.preventDefault();

            var valid = true;

            $(this).find('input.required, input.number').each(function() {
                if (!$(form).validate().element(this)) {
                    valid = false;
                }
            });
            if (!valid) {
                return false;
            }

            var name = '';
            var quantity = '';
            var units = '';
            var sid = '';
            var eid = '';

            switch (type) {
                case 'section':
                    name = $(addDiv).find('div.bomSectionNameInput input').val();
                    break;
                case 'element':
                    name = $(addDiv).find('div.bomElementNameInput input').val();
                    quantity = $(addDiv).find('div.bomElementQuantityInput input').val();
                    units = $(addDiv).find('div.bomElementUnitsInput input').val();
                    sid = $(addDiv).closest('li.bomSection').index() + 1;
                    break;
                case 'material':
                    name = $(addDiv).find('div.bomMaterialNameInput input').val();
                    quantity = $(addDiv).find('div.bomMaterialQuantityInput input').val();
                    sid = $(addDiv).closest('li.bomSection').index() + 1;
                    eid = $(addDiv).closest('li.bomElement').index() + 1;
                    break;
            }

            var params = 'name=' + name + '&quantity=' + quantity + '&units=' + units + '&sid=' + sid + '&eid=' + eid;
            var addUrl = _projectUrl + _projectId + '/newitem';
            $.ajax({
                type: 'POST',
                url: addUrl,
                data: params,
                success: function(data){
                    var json = $.parseJSON(data);
                    alert(json.name);
                },
                error: function(e){
                    alert('Error adding ' + type + ': ' + e);
                },
            });
        });
    }

    function _assignEditSubmitCancelOptionEventHandlers(editDiv) {
        var type = _getType(editDiv);

        $(editDiv).find('input.bomSubmit').click(function(e) {
            e.preventDefault();
            $(this).closest('form').submit();
        });

        $(editDiv).find('input.bomCancel').click(function(e) {
            e.preventDefault();
            $(editDiv).children('form').remove();
            $(editDiv).children('div').show();
        });

        var form = $(editDiv).find('form');
        form.validate();
        form.submit(function (e) {
            e.preventDefault();

            var valid = true;

            $(this).find('input.required, input.number').each(function() {
                if (!$(form).validate().element(this)) {
                    valid = false;
                }
            });
            if (!valid) {
                return false;
            }

            var name = '';
            var quantity = '';
            var units = '';
            var sid = $(editDiv).closest('li.bomSection').index() + 1;
            var eid = '';
            var mid = '';

            switch (type) {
                case 'section':
                    name = $(editDiv).find('div.bomSectionNameInput input').val();
                    break;
                case 'element':
                    name = $(editDiv).find('div.bomElementNameInput input').val();
                    quantity = $(editDiv).find('div.bomElementQuantityInput input').val();
                    units = $(editDiv).find('div.bomElementUnitsInput input').val();
                    eid = $(editDiv).closest('li.bomElement').index() + 1;
                    break;
                case 'material':
                    name = $(editDiv).find('div.bomMaterialNameInput input').val();
                    quantity = $(editDiv).find('div.bomMaterialQuantityInput input').val();
                    eid = $(editDiv).closest('li.bomElement').index() + 1;
                    mid = $(editDiv).closest('li.bomMaterial').index() + 1;
                    break;
            }

            var params = 'name=' + name + '&quantity=' + quantity + '&units=' + units + '&sid=' + sid + '&eid=' + eid + '&mid=' + mid;
            var editUrl = _projectUrl + _projectId + '/edititem';
            $.ajax({
                type: 'POST',
                url: editUrl,
                data: params,
                success: function(data){
                    var json = $.parseJSON(data);
                    alert(json.name);
                },
                error: function(e){
                    alert('Error editing ' + type + ': ' + e);
                },
            });
        });
    }

    function _displayAddForm(node) {
        var addDiv = $(node).closest('li.addToBOM');
        $(addDiv).html('<form>' + _renderFormInputs(node) + _renderSubmitCancelOptions(_addText, _cancelText) + '</form>');
        _assignAddSubmitCancelOptionEventHandlers(addDiv);
    }

    function _displayEditForm(node) {
        var sid = $(node).closest('li.bomSection').index();
        var field = new Array();
        switch(_getType(node)) {
            case 'material':
                var eid = $(node).closest('li.bomElement').index();
                var mid = $(node).closest('li.bomMaterial').index();
                field = _data.sections[sid].elements[eid].materials[mid];
                break;
            case 'element':
                var eid = $(node).closest('li.bomElement').index();
                field = _data.sections[sid].elements[eid];
                break;
            default:
                field = _data.sections[sid];
        }
        $(node).children('div').hide();
        $(node).append('<form>' + _renderFormInputs(node, field) + _renderSubmitCancelOptions(_editText, _cancelText) + '</form>');
        _assignEditSubmitCancelOptionEventHandlers(node);
    }

    function _renderSubmitCancelOptions(submitText, cancelText) {
        var html = '<div class="bomAddSubmitCancelOptions"><input type="submit" class="bomSubmit" value="' + submitText + '" />';
        return html + '<input type="button" class="bomCancel" value="' + cancelText + '" /></div>';
    }

    function _renderAddControl(type) {
        return '<li class="addToBOM">' + _renderAddControlText(type) + '</li>';
    }

    function _renderFormInputs(node, field) {
        if (field == undefined) { field = new Array(); }

        var name = (field.name != undefined) ? field.name : '';
        var units = (field.units != undefined) ? field.units : '';
        var quantity = (field.quantity != undefined) ? field.quantity : '';
        html = '';

        switch(_getType(node)) {
            case "element":
                var sid = $(node).closest('li.bomSection').index();
                html += '<div class="bomElementNameInput"><input type="text" class="required" id="bomS' + sid + 'ElementName" value="' + name + '" /></div>';
                html += '<div class="bomElementQuantityInput"><input type="text" class="required number" id="bomS' + sid + 'ElementQuantity" value="' + quantity + '" /></div>'
                html += '<div class="bomElementUnitsInput"><input type="text" id="bomS' + sid + 'ElementUnits" value="' + units + '" /></div>'
                break;
            case "material":
                var sid = $(node).closest('li.bomSection').index();
                var eid = $(node).closest('li.bomElement').index();
                html = '<div class="bomMaterialNameInput"><input type="text" class="required" id="bomS' + sid + 'E' + eid + 'MaterialName" value="' + name + '" /></div>';
                html += '<div class="bomMaterialQuantityInput"><input type="text" class="required number" id="bomS' + sid + 'E' + eid + 'MaterialQuantity" value="' + quantity + '" /></div>'
                break;
            default:
                html += '<div class="bomSectionNameInput"><input type="text" class="required" id="bomSectionName" value="' + name + '" /></div>';
        }
        return html;
    }

    function _renderAddControlText(type) {
        var returnText = _addMaterialText;
        switch(type) {
            case "section":
                returnText = _addSectionText;
                break;
            case "element":
                returnText = _addElementText;
                break;
        }
        return '<a>' + returnText + '</a>';
    }

    function _confirmDelete(node) {
        var type = _getType(node);
        if (confirm(_deleteConfirmText + ' ' + type + '?')) {
            var sid = $(node).closest('li.bomSection').index() + 1;
            var eid = '';
            var mid = '';

            switch(type) {
                case 'material':
                    eid = $(node).closest('li.bomElement').index() + 1;
                    mid = $(node).closest('li.bomMaterial').index() + 1;
                    break;
                case 'element':
                    eid = $(node).closest('li.bomElement').index() + 1;
                    break;
            }
            var params = 'sid=' + sid + '&eid=' + eid + '&mid=' + mid;
            var deleteUrl = _projectUrl + _projectId + '/deleteitem';

            $.ajax({
                type: 'POST',
                url: deleteUrl,
                data: params,
                success: function(data){
                    var json = $.parseJSON(data);
                    alert(json.name);
                },
                error: function(e){
                    alert('Error deleting ' + type + ': ' + e);
                },
            });
        }
    }

    function _renderSection(section) {
        var html = '<li class="bomSection"><div class="deleteFromBOM"><a>';
        html += _deleteText + '</a></div><div class="bomEditable"><div class="bomSName">';
        html += _readValue(section.name);
        html += '</div><div class="bomSEnergy">';
        html += '</div><div class="bomSCarbon">';
        html += '</div></div><ul class="bomElements">';

        var elements = [];
        $(section.elements).each(function(index, element){
            elements[index] = _renderElement(element);
        });
        html += elements.join('');

        html += _renderAddControl('element') + '</ul></li>';

        return html;
    }

    function _renderElement(element) {
        var html = '<li class="bomElement"><div class="deleteFromBOM"><a>';
        html += _deleteText + '</a></div><div class="bomEditable"><div class="bomEName">';
        html += _readValue(element.name);
        html += '</div><div class="bomEQuantity">';
        html += _readValue(element.quantity);
        html += '</div><div class="bomEUnits">';
        html += _readValue(element.units);
        html += '</div><div class="bomEEnergy">';
        html += '</div><div class="bomECarbon">';
        html += '</div></div><ul class="bomMaterials">';

        var materials = [];
        $(element.materials).each(function(index, material){
            materials[index] = _renderMaterial(material);
        });
        html += materials.join('');

        html += _renderAddControl('material') + '</ul></li>';

        return html;
    }

    function _renderMaterial(material) {
        var html = '<li class="bomMaterial"><div class="deleteFromBOM"><a>';
        html += _deleteText + '</a></div><div class="bomEditable"><div class="bomMName">';
        html += _readValue(material.name);
        html += '</div><div class="bomMQuantity">';
        html += _readValue(material.quantity);
        html += '</div><div class="bomMUnits">';
        html += _readValue(material.units);
        html += '</div><div class="bomMEnergy">';
        html += _readValue(material.totalEnergy);
        html += '</div><div class="bomMCarbon">';
        html += _readValue(material.totalCarbon);
        html += '</div></div></li>';

        return html;
    }

    function _renderHeader() {
        html = '<div class="bomHName">';
        html += _headerNameText;
        html += '</div><div class="bomHQuantity">';
        html += _headerQuantityText;
        html += '</div><div class="bomHEnergy">';
        html += _headerEnergyText;
        html += '</div><div class="bomHCarbon">';
        html += _headerCarbonText;
        html += '</div><div class="bomHClear"></div>';

        return html;
    }

    function _renderFooter() {
        html = '<div class="bomFSummary">';
        html += _footerSummaryText;
        html += '</div><div class="bomFEnergy">';
        html += '</div><div class="bomFCarbon">';
        html += '</div><div class="bomFClear"></div>';

        return html;
    }

    function _getType(node) {
        var level = $(node).closest('ul').attr('class');
        var type = 'section';
        switch(level) {
            case 'bomElements':
                type = 'element';
                break;
            case 'bomMaterials':
                type = 'material';
                break;
        }
        return type;
    }

    function _readValue(value) {
        var returnVal = '';
        if (value != undefined && value != '') {
            returnVal = value;
        }
        return returnVal
    }

}

BillOfMaterials.prototype = {
    render : function() { this._render(); }
}
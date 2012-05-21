function SustainabilitySummary (config) {
    if (config == undefined) { config = new Array(); }
    var _data = new Array();
    var _target = new Array();
    var _div = (config.div != undefined) ? config.div : 'div#sustainabilitySummary';
    var _projectId = (config.projectId != undefined) ? config.projectId : 1;
    var _targetId = (config.targetId != undefined) ? config.targetId : 0;
    var _projectUrl = (config.projectUrl != undefined) ? config.projectUrl : './projects/';
    var _headingText = (config.headingText != undefined) ? config.headingText : 'Sustainability Summary';
    var _percentageCompleteText = (config.percentageCompleteText != undefined) ? config.percentageCompleteText : '<span>[percentage]</span> detailed';
    var _operationalCompleteText = (config.operationalCompleteText != undefined) ? config.operationalCompleteText : '<span>[detailed] of [total]</span> energy uses detailed.';
    var _constructionCompleteText = (config.constructionCompleteText != undefined) ? config.constructionCompleteText : '<span>[detailed] of [total]</span> elements detailed.';
    var _operationalFootprintText = (config.operationalFootprintText != undefined) ? config.operationalFootprintText : 'Operational';
    var _carbonText = (config.carbonText != undefined) ? config.carbonText : 'Carbon (CO<sub>2</sub>)';
    var _billOfMaterialsText = (config.billOfMaterialsText != undefined) ? config.billOfMaterialsText : 'Embodied';
    var _totalCarbonText = (config.totalCarbonText != undefined) ? config.totalCarbonText : 'Total';
    var _perPersonCarbonText = (config.perPersonCarbonText != undefined) ? config.perPersonCarbonText : 'Per Person';
    var _carbonUnitsText = (config.carbonUnitsText != undefined) ? config.carbonUnitsText : 'g';
    var _carbonPerPersonUnitsText = (config.carbonPerPersonUnitsText != undefined) ? config.carbonPerPersonUnitsText : 'kg';
    var _compareButtonText = (config.compareButtonText != undefined) ? config.compareButtonText : 'Compare to other projects';

    var summaryUrl = _projectUrl + _projectId + '/summary.json';
    var targetUrl = _projectUrl + _targetId + '/summary.json';

    var contentDiv = $('<div/>', { 'class': 'sSummaryContent' }).appendTo(_div);
    $('<h3/>', { html: _headingText }).appendTo(contentDiv);
    $('<div/>', { 'class': 'summaryCompare', 'html':
        '<button>' + _compareButtonText + '</button>'
    }).appendTo(contentDiv);
    $('<div/>', { 'class': 'completionSummary' }).appendTo(contentDiv);
    $('<div/>', { 'class': 'carbonSummary' }).appendTo(contentDiv);

    $(_div + ' div.summaryCompare button').button().click(function() {
       alert('Compare project');
    });

    var top = 0;
    $(window).scroll(function (event) {
        top = $(_div).offset().top - parseFloat($(_div).css('marginTop').replace(/auto/,0));

        var y = $(this).scrollTop();

        if (y >= top) {
          if (!$(_div + ' div.sSummaryContent').hasClass('summaryFixed')) {
              $(_div + ' div.sSummaryContent').addClass('summaryFixed');
              $(_div + ' div.sSummaryContent').css('top', '0px');
          }

        } else {
          $(_div + ' div.sSummaryContent').removeClass('summaryFixed');
        }
    });

    if (_targetId > 0) {
        $.getJSON(targetUrl, function(data){
            _target = data;
            _refresh();
        });
    } else {
        _refresh();
    }


    this._update = function() {
        _refresh();
    };


    function _refresh() {
        $.getJSON(summaryUrl, function(data){
            _data = data;
            _redraw();
        });
    };

    function _redraw() {

        var completionSummary = $('<div/>');

        $('<h4/>', { 'html': _percentageCompleteText.replace('[percentage]',
                _data.percentComplete) }).appendTo(completionSummary);

        var completeUl = $('<ul/>').appendTo(completionSummary);

        $('<li/>', { 'class': 'operationalComplete', 'html':
            _operationalCompleteText.replace('[detailed]', _data.elOperationalDetailed)
                .replace('[total]', _data.elOperationalTotal)
        }).appendTo(completeUl);

        $('<li/>', { 'class': 'constructionComplete', 'html':
            _constructionCompleteText.replace('[detailed]', _data.elConstructionDetailed)
                .replace('[total]', _data.elConstructionTotal)
        }).appendTo(completeUl);

        $(_div + ' div.completionSummary').html(completionSummary);

        // Set the minimum value - default to zero
        var min = _findMinValue(_data.perOccupantCarbonChange, 0);
        min = _findMinValue(_target.perOccupantCarbonChange, min);

        var chartOptions = {
                grid: { borderWidth: 1 },
                xaxis: {
                    tickFormatter: function() { return ""; }
                },
                yaxis: {
                    min: min
                },
                legend: {
                    display: true,
                    labelBoxBorderColor: null,
                    container: _div + ' div.carbonSummary div.summaryTotalLegend'
                }
        };

        $(_div + ' div.carbonSummary').html(_renderSummaryTable(
                _data.carbonOperational, _data.carbonConstruction,
                _data.carbonTotal, _data.carbonPerOccupant,
                _carbonText, _totalCarbonText, _perPersonCarbonText,
                _carbonUnitsText, _carbonPerPersonUnitsText));

        var chartData = {
                data: _data.perOccupantCarbonChange,
                label: _data.name
        }

        if (_target.perOccupantCarbonChange == undefined) {
            $.plot($(_div + ' div.carbonSummary div.summaryTotalChange'),
                    [ chartData ], chartOptions);
        } else {
            var target = new Array();
            var counter = 0;
            $(_data.perOccupantCarbonChange).each(function(index, changeValue) {
                target[counter] = [counter, _target.carbonPerOccupant];
                counter++;
            });

            var targetData = {
                    data: target,
                    label: _target.name
            }

            $.plot($(_div + ' div.carbonSummary div.summaryTotalChange'),
                    [ chartData, targetData ], chartOptions);
        }
    }

    function _renderSummaryTable(totalOperating, totalConstruction,
            total, perOccupant, headingText, totalText, perPersonText,
            unitsText, perPersonUnitsText) {

        var summaryTable = '<h4>';
        summaryTable += headingText;
        summaryTable += '</h4><table><tbody>';
        summaryTable += '<tr><td>';
        summaryTable += _operationalFootprintText;
        summaryTable += '</td><td class="summaryValue">';
        summaryTable += _formatNumber(totalOperating);
        if (unitsText != '') {
            summaryTable += ' ' + unitsText;
        }
        summaryTable += '</td></tr><tr><td>';
        summaryTable += _billOfMaterialsText;
        summaryTable += '</td><td class="summaryValue">';
        summaryTable += _formatNumber(totalConstruction);
        if (unitsText != '') {
            summaryTable += ' ' + unitsText;
        }
        summaryTable += '</td></tr><tr class="sSummaryTotal"><td>';
        summaryTable += totalText;
        summaryTable += '</td><td class="summaryValue">';
        summaryTable += _formatNumber(total);
        if (unitsText != '') {
            summaryTable += ' ' + unitsText;
        }
        summaryTable += '</td></tr><tr class="sSummaryPerPerson"><td>';
        summaryTable += perPersonText;
        summaryTable += '</td><td class="summaryValue">';
        summaryTable += _formatNumber(perOccupant);
        if (perPersonUnitsText != '') {
            summaryTable += ' ' + perPersonUnitsText;
        }
        summaryTable += '</td></tr></tbody></table>';
        summaryTable += '<div class="summaryTotalChange"></div>';
        summaryTable += '<div class="summaryTotalLegend"></div>';

        return summaryTable;
    }

    function _formatNumber(value) {
        return (Math.round(value * 10) / 10).toFixed(1);
    }

    function _findMinValue(data, min) {
        if (min == undefined) {
            min = 0;
        }
        if (data != undefined) {
            $(data).each(function(index, changeValue){
                var parsedValue = parseFloat(changeValue);
                if (parsedValue < min) {
                    min = parsedValue;
                }
            });
        }
        return min;
    }
}

SustainabilitySummary.prototype = {
    update : function() { this._update(); }
};
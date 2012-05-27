function SustainabilitySummary (config) {
    if (config == undefined) { config = new Array(); }
    var _data = new Array();
    var _target = new Array();
    var _comparisonData = new Object();
    var _div = (config.div != undefined) ? config.div : 'div#sustainabilitySummary';
    var _publicId = (config.publicId != undefined) ? config.publicId : 0;
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
    var _perPersonCarbonGraphText = (config.perPersonCarbonGraphText != undefined) ? config.perPersonCarbonGraphText : 'Per Person Carbon History';
    var _compareButtonText = (config.compareButtonText != undefined) ? config.compareButtonText : 'Compare to other projects';
    var _comparisonProjectsText = (config.comparisonProjectsText != undefined) ? config.comparisonProjectsText : 'Select the projects to compare to:';

    var summaryUrl = _projectUrl + 'share/' + _publicId + '/summary.json';
    var comparisonUrl = _projectUrl + 'share/' + _publicId + '/compare.json';
    var targetUrl = _projectUrl + _targetId + '/summary.json';

    var contentDiv = $('<div/>', { 'class': 'sSummaryContent' }).appendTo(_div);
    $('<h3/>', { html: _headingText }).appendTo(contentDiv);
    $('<div/>', { 'class': 'summaryCompare', 'html':
        '<button>' + _compareButtonText + '</button>'
    }).appendTo(contentDiv);
    $('<div/>', { 'class': 'completionSummary' }).appendTo(contentDiv);
    $('<div/>', { 'class': 'carbonSummary' }).appendTo(contentDiv);

    var comparisonDiv = $('<div/>', { 'class': 'comparison', 'style': 'display: none;' })
        .appendTo(contentDiv);

    $(_div + ' div.summaryCompare button').button().colorbox({
        href: _div + ' div.comparisonContent',
        inline: true,
        width: '99%',
        height: '99%',
        onComplete: function() {
            _refreshComparison();
        }
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

    $.getJSON(comparisonUrl, function(data){
        _renderComparison(data, comparisonDiv);
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
                    min: min,
                    tickFormatter: function (v) {
                        return v + ' ' + _carbonPerPersonUnitsText;
                    }
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
        };

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
                    label: _target.name + ' (' + _target.carbonPerOccupant
                        + ' ' + _carbonPerPersonUnitsText + ')'
            };

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
        summaryTable += '<h5>' + _perPersonCarbonGraphText + '</h5>';
        summaryTable += '<div class="summaryTotalChange"></div>';
        summaryTable += '<div class="summaryTotalLegend"></div>';

        return summaryTable;
    }

    function _renderComparison(projects, comparisonDiv) {

        var html = '<div class="comparisonContent">';
        html += '<div class="comparisonProjects"><h4>';
        html += _comparisonProjectsText;
        html += '</h4>';

        if (projects.project.id != undefined) {
            html += '<ul class="currentProject">';
            html += '<li><label>' + projects.project.name + '</label></li>';
            html += '</ul>';
        }

        if (projects.comparable != undefined) {
            var cHtml = '';
            $(projects.comparable).each(function(index, comparableProject) {
                cHtml += '<li><div class="comparableInput">';
                cHtml += '<input type="checkbox" value="c_' + _pad(index, 5)
                    + '_' + comparableProject.id + '" />';
                cHtml += '</div><label>';
                cHtml += comparableProject.name;
                cHtml += '</label></li>';
            });

            if (cHtml != '') {
                html += '<ul class="comparableProjects">';
                html += cHtml;
                html += '</ul>';
            }
        }

        if (projects.user != undefined) {
            var uHtml = '';
            $(projects.user).each(function(index, userProject) {
                uHtml += '<li><div class="comparableInput">';
                uHtml += '<input type="checkbox" value="u_' + _pad(index, 5)
                    + '_' + userProject.id + '" />';
                uHtml += '</div><label>';
                uHtml += userProject.name;
                uHtml += '</label></li>';
            });

            if (uHtml != '') {
                html += '<ul class="userProjects">';
                html += uHtml;
                html += '</ul>';
            }
        }

        html += '</div><div class="comparisonGraphs">';
        html += '<h2>' + _perPersonCarbonText + ' ' + _carbonText + '</h2>';
        html += '<div class="comparisonPerOccupantCarbon"></div>';
        html += '<h2>' + _totalCarbonText + ' ' + _carbonText + '</h2>';
        html += '<div class="comparisonTotalCarbon"></div>';
        html += '</div></div>';

        $(comparisonDiv).html(html);

        $(_div + ' div.comparableInput input').change(function() {
            var key = $(this).val();
            var id = key.substring(key.lastIndexOf('_') + 1, key.length);

            if ($(this).is(':checked')) {
                var url = _projectUrl + id + '/summary.json';
                $.getJSON(url, function(data){
                    _comparisonData[key] = data;
                    _refreshComparison();
                });
            } else {
                delete _comparisonData[key];
                _refreshComparison();
            }
        });
    }

    function _refreshComparison() {
        _drawComparisonGraph('perOccupant', 'div.comparisonPerOccupantCarbon');
        _drawComparisonGraph('total', 'div.comparisonTotalCarbon');
    }

    function _drawComparisonGraph(type, div) {

        var operationalData = new Array();
        var embodiedData = new Array();
        var ticks = new Array();
        var min = 0;

        ticks[0] = [0, ''];
        ticks[1] = [1, _data.name];

        switch (type) {
            case 'perOccupant':
                var operational = parseFloat(_data.carbonPerOccupantOperational);
                var embodied = parseFloat(_data.carbonPerOccupantConstruction);

                if (operational + embodied < min) {
                    min = operational + embodied;
                }

                operationalData[0] = [ 1, operational];
                embodiedData[0] = [ 1, embodied];
                break;
            case 'total':
                var operational = parseFloat(_data.carbonOperational) / 1000;
                var embodied = parseFloat(_data.carbonConstruction) / 1000;

                if (operational + embodied < min) {
                    min = operational + embodied;
                }
                operationalData[0] = [ 1, operational];
                embodiedData[0] = [ 1, embodied];
                break;
        }


        var orderedKeys = _keys(_comparisonData);

        $(orderedKeys).each(function(index, key) {

            var data = _comparisonData[key];
            var index = operationalData.length;
            var ticker = index + 1;

            if (data != undefined) {

                ticks[ticker] = [ticker, data.name];

                switch (type) {
                    case 'perOccupant':
                        var operational = parseFloat(data.carbonPerOccupantOperational);
                        var embodied = parseFloat(data.carbonPerOccupantConstruction);

                        if (operational + embodied < min) {
                            min = operational + embodied;
                        }

                        operationalData[index] = [ ticker, operational];
                        embodiedData[index] = [ ticker, embodied];
                        break;
                    case 'total':
                        var operational = parseFloat(data.carbonOperational) / 1000;
                        var embodied = parseFloat(data.carbonConstruction) / 1000;

                        if (operational + embodied < min) {
                            min = operational + embodied;
                        }
                        operationalData[index] = [ ticker, operational];
                        embodiedData[index] = [ ticker, embodied];
                        break;
                }
            }
        });

        var maxIndex = operationalData.length;
        var maxTicker = maxIndex + 1;

        ticks[maxTicker] = [maxTicker, ''];
        operationalData[maxIndex] = [ maxTicker, null];
        embodiedData[maxIndex] = [ maxTicker, null];

        var chartOptions = {
                grid: { borderWidth: 1 },
                xaxis: {
                    min: 0,
                    ticks: ticks,
                    tickLength: 4
                },
                yaxis: {
                    min: min,
                    tickFormatter: function (v) {
                        return v + ' ' + _carbonPerPersonUnitsText;
                    }
                },
                legend: {
                    display: true,
                    labelBoxBorderColor: null
                },
                series: {
                    stack: 0,
                    bars: { show: true, barWidth: 0.7, align: 'center' }
                }
        };

        var width = $(div).parent('div').width() - 50;
        var height = ($(div).parent('div').height() / 2) - 45;

        $(div).width(width + 'px');
        $(div).height(height + 'px');

        $.plot(div, [{ label: _operationalFootprintText, data: operationalData },
                     { label: _billOfMaterialsText, data: embodiedData }],
                     chartOptions);
    }

    function _keys(obj) {
        var keys = [];

        for(var key in obj) {
            if(obj.hasOwnProperty(key)) {
                keys.push(key);
            }
        }
        return keys.sort();
    }

    function _pad(number, length) {

        var str = '' + number;
        while (str.length < length) {
            str = '0' + str;
        }
        return str;

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
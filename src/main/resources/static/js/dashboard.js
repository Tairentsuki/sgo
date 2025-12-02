document.addEventListener("DOMContentLoaded", function() {
    const bridge = document.getElementById('bridge');
    if(!bridge) return;

    function read(cls) {
        const els = document.querySelectorAll('.'+cls);
        const labels = []; const values = [];
        els.forEach(el => { labels.push(el.dataset.k); values.push(parseFloat(el.dataset.v)); });
        return { labels, values };
    }

    Chart.defaults.font.family = "'Inter', sans-serif";
    Chart.defaults.color = '#64748b';

    const dRank = read('d-rank');
    new Chart(document.getElementById('chartRanking'), {
        type: 'bar',
        data: {
            labels: dRank.labels,
            datasets: [{ label: 'Custo Total', data: dRank.values, backgroundColor: '#3b82f6', borderRadius: 6, barThickness: 30 }]
        },
        options: { indexAxis: 'y', responsive: true, maintainAspectRatio: false, plugins: { legend: {display: false} }, scales: { x: {grid: {display:false}}, y: {grid: {display:false}} } }
    });

    const dProf = read('d-prof');

    const palette = [
        '#3b82f6',
        '#10b981',
        '#f59e0b',
        '#ef4444',
        '#8b5cf6',
        '#06b6d4',
        '#84cc16',
        '#f97316',
        '#6366f1',
        '#14b8a6',
        '#64748b',
        '#d946ef',
        '#fbbf24',
        '#9ca3af',
        '#a855f7',
        '#22c55e',
        '#0ea5e9',
        '#eab308'
    ];

    new Chart(document.getElementById('chartProfissao'), {
        type: 'doughnut',
        data: {
            labels: dProf.labels,
            datasets: [{
                data: dProf.values,
                backgroundColor: palette,
                borderWidth: 0,
                hoverOffset: 10
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '70%',
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {boxWidth:10, usePointStyle:true}
                }
            }
        }
    });
});
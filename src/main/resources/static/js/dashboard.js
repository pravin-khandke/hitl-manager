// HITL-Manager 2.0 Dashboard Charts

document.addEventListener('DOMContentLoaded', () => {
    if (document.getElementById('scatter-chart')) initScatterChart();
    if (document.getElementById('bubble-chart')) initBubbleChart();
});

document.body.addEventListener('htmx:afterSwap', (event) => {
    if (event.detail.target.id === 'charts-container') {
        if (document.getElementById('scatter-chart')) initScatterChart();
        if (document.getElementById('bubble-chart')) initBubbleChart();
    }
});

let scatterChartInstance = null;
let bubbleChartInstance = null;

async function initScatterChart() {
    const ctx = document.getElementById('scatter-chart');
    if (!ctx) return;
    try {
        const resp = await fetch('/api/charts/scatter');
        const data = await resp.json();
        if (scatterChartInstance) scatterChartInstance.destroy();
        scatterChartInstance = new Chart(ctx, {
            type: 'scatter',
            data: {
                datasets: [{
                    label: 'Response Time vs AI Confidence',
                    data: data.points || [],
                    backgroundColor: 'rgba(59, 130, 246, 0.5)',
                    borderColor: 'rgba(59, 130, 246, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } },
                scales: {
                    x: { title: { display: true, text: 'AI Confidence (%)' }, min: 0, max: 100 },
                    y: { title: { display: true, text: 'Response Time (s)' }, min: 0 }
                }
            }
        });
    } catch (e) { console.error('Failed to load scatter data:', e); }
}

async function initBubbleChart() {
    const ctx = document.getElementById('bubble-chart');
    if (!ctx) return;
    try {
        const resp = await fetch('/api/charts/mesh');
        const data = await resp.json();
        if (bubbleChartInstance) bubbleChartInstance.destroy();
        bubbleChartInstance = new Chart(ctx, {
            type: 'bubble',
            data: {
                datasets: [{
                    label: 'System Accuracy',
                    data: data.points || [],
                    backgroundColor: 'rgba(16, 185, 129, 0.5)',
                    borderColor: 'rgba(16, 185, 129, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } },
                scales: {
                    x: { title: { display: true, text: 'Feedback Volume (actions)' } },
                    y: { title: { display: true, text: 'Operator Experience Score' }, min: 0, max: 100 }
                }
            }
        });
    } catch (e) { console.error('Failed to load mesh data:', e); }
}

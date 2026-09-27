import React, { useState, useEffect, useRef } from 'react';
import {
  Activity,
  Heart,
  Zap,
  Shield,
  Radio,
  Sliders,
  AlertTriangle,
  FileText,
  Lock,
  WifiOff,
  Database,
  RefreshCw,
  Send,
  UserCheck,
  Smartphone,
  Flame,
  Wind,
  CheckCircle2,
  Bell,
  HardDrive,
  Trash2,
  Play,
  RotateCcw,
  Cpu,
  BarChart3,
  Layers,
  ChevronRight
} from 'lucide-react';

export default function App() {
  // Navigation State: 10 Core Screens
  const [activeTab, setActiveTab] = useState('dashboard');

  // Simulation Scenario
  const [scenario, setScenario] = useState('NORMAL'); // NORMAL, ARRHYTHMIA, FALL, HEAT, HYPOXIA

  // Telemetry & Baseline state
  const [vitals, setVitals] = useState({
    hr: 72,
    spo2: 98,
    temp: 36.6,
    accelX: 0.02,
    accelY: 0.98,
    accelZ: 0.04,
    ambientTemp: 28.5,
    humidity: 55,
    aqi: 42,
    sqi: 98.2
  });

  const [baseline] = useState({
    hrMean: 70,
    hrStdDev: 6.2,
    spo2Baseline: 98.2,
    confidence: 94,
    samples: 1440
  });

  // AI Risk Assessment State
  const [assessment, setAssessment] = useState({
    score: 12,
    level: 'LOW',
    primaryThreat: 'Normal Physiological Rhythm',
    factors: [],
    inferenceTime: 3
  });

  // Alerts State
  const [alerts, setAlerts] = useState([]);

  // Emergency Queue & Disaster payload state
  const [disasterQueue, setDisasterQueue] = useState([]);
  const [emergencySmsPayload, setEmergencySmsPayload] = useState(null);
  const [meshActive, setMeshActive] = useState(true);

  // Privacy Settings state
  const [privacy, setPrivacy] = useState({
    hrConsent: true,
    spo2Consent: true,
    motionConsent: true,
    locationConsent: true,
    zeroCloud: true,
    keystoreEncrypted: true
  });

  // Canvas ECG trace ref
  const canvasRef = useRef(null);

  // Continuous Telemetry Generator & AI Evaluation Loop
  useEffect(() => {
    const interval = setInterval(() => {
      setVitals(prev => {
        let hr = prev.hr;
        let spo2 = prev.spo2;
        let temp = prev.temp;
        let aqi = prev.aqi;
        let accelX = 0.02 + (Math.random() - 0.5) * 0.05;
        let accelY = 0.98 + (Math.random() - 0.5) * 0.05;
        let accelZ = 0.04 + (Math.random() - 0.5) * 0.05;
        let ambientTemp = 28.5;
        let humidity = 55;

        if (scenario === 'NORMAL') {
          hr = Math.min(78, Math.max(68, hr + (Math.random() - 0.5) * 2));
          spo2 = Math.min(99, Math.max(97.5, spo2 + (Math.random() - 0.5) * 0.4));
          temp = 36.6 + (Math.random() - 0.5) * 0.1;
          aqi = 42 + (Math.random() - 0.5) * 4;
        } else if (scenario === 'ARRHYTHMIA') {
          hr = Math.min(165, Math.max(135, hr + (Math.random() - 0.4) * 6));
          spo2 = 96.2;
          temp = 37.1;
        } else if (scenario === 'FALL') {
          accelX = 2.85 + (Math.random() - 0.5) * 0.6;
          accelY = 3.65 + (Math.random() - 0.5) * 0.6;
          accelZ = 1.95 + (Math.random() - 0.5) * 0.4;
          hr = 126;
        } else if (scenario === 'HEAT') {
          ambientTemp = 42.5;
          humidity = 82;
          temp = Math.min(39.4, temp + 0.1);
          hr = Math.min(132, hr + 1.5);
          aqi = 88;
        } else if (scenario === 'HYPOXIA') {
          spo2 = Math.max(84, spo2 - 1.2);
          hr = Math.min(118, hr + 1.2);
          aqi = 185;
        }

        // Evaluate Risk Engine
        const motionMag = Math.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        const sqi = motionMag > 2.5 ? 64.5 : 98.2;
        const heatIndex = ambientTemp + 0.33 * (humidity / 100 * 6.105 * Math.exp(17.27 * ambientTemp / (237.7 + ambientTemp))) - 4.0;

        // XAI Factors calculation
        const factors = [];
        let riskScore = 8;

        const hrZ = (hr - baseline.hrMean) / baseline.hrStdDev;
        if (Math.abs(hrZ) > 2.0 || hr > 120) {
          const impact = Math.min(45, Math.round(Math.abs(hrZ) * 12 + (hr > 130 ? 18 : 0)));
          riskScore += impact;
          factors.push({
            name: "Cardiovascular Spike / Arrhythmia",
            impact: impact,
            status: `HR at ${Math.round(hr)} BPM (Z-Score: ${hrZ.toFixed(1)})`,
            comparison: `+${Math.round(hr - baseline.hrMean)} BPM vs baseline`,
            severity: impact > 30 ? "CRITICAL" : "ELEVATED"
          });
        }

        if (spo2 < 95) {
          const impact = spo2 < 90 ? 50 : Math.round((baseline.spo2Baseline - spo2) * 8);
          riskScore += impact;
          factors.push({
            name: "Blood Oxygen Hypoxia Risk",
            impact: impact,
            status: `SpO₂ dropped to ${Math.round(spo2)}%`,
            comparison: `-${(baseline.spo2Baseline - spo2).toFixed(1)}% below normal baseline`,
            severity: spo2 < 90 ? "CRITICAL" : "HIGH"
          });
        }

        if (motionMag > 3.0) {
          riskScore += 45;
          factors.push({
            name: "Sudden High-G Impact / Fall",
            impact: 45,
            status: `Accel Vector: ${motionMag.toFixed(2)}G`,
            comparison: "Sudden impact spike followed by immobility",
            severity: "CRITICAL"
          });
        }

        if (heatIndex > 38 || temp > 38) {
          const impact = heatIndex > 42 ? 35 : 20;
          riskScore += impact;
          factors.push({
            name: "Heat Stroke / Thermal Exhaustion",
            impact: impact,
            status: `Heat Index: ${heatIndex.toFixed(1)}°C / Core: ${temp.toFixed(1)}°C`,
            comparison: `Ambient humidity (${humidity}%) exacerbating strain`,
            severity: impact > 30 ? "CRITICAL" : "ELEVATED"
          });
        }

        if (aqi > 150) {
          const impact = Math.min(25, Math.round((aqi - 100) / 10));
          riskScore += impact;
          factors.push({
            name: "Hazardous AQI Pollution Impact",
            impact: impact,
            status: `AQI index at ${Math.round(aqi)} (Unhealthy)`,
            comparison: "Fine particulate matter exposure",
            severity: "ELEVATED"
          });
        }

        const finalScore = Math.min(99, riskScore);
        const level = finalScore >= 80 ? 'CRITICAL' : finalScore >= 55 ? 'HIGH' : finalScore >= 25 ? 'ELEVATED' : 'LOW';
        const primary = factors.length > 0 ? factors.sort((a, b) => b.impact - a.impact)[0].name : 'Normal Physiological Rhythm';

        setAssessment({
          score: finalScore,
          level: level,
          primaryThreat: primary,
          factors: factors,
          inferenceTime: 3
        });

        // Trigger Alerts if critical
        if (finalScore >= 50 && !alerts.some(a => a.title === primary)) {
          setAlerts(old => [{
            id: Date.now().toString(),
            title: primary,
            message: `Automated Edge Guard: ${primary} detected.`,
            severity: level,
            timestamp: new Date().toLocaleTimeString()
          }, ...old]);
        }

        return {
          hr, spo2, temp, accelX, accelY, accelZ, ambientTemp, humidity, aqi, sqi
        };
      });
    }, 800);

    return () => clearInterval(interval);
  }, [scenario, baseline, alerts]);

  // Dynamic Waveform Canvas Visualizer
  useEffect(() => {
    if (!canvasRef.current || activeTab !== 'live_monitor') return;
    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');
    let animId;
    let offset = 0;

    const render = () => {
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      const width = canvas.width;
      const height = canvas.height;

      // Draw Grid
      ctx.strokeStyle = '#1C273E';
      ctx.lineWidth = 1;
      for (let x = 0; x < width; x += 20) {
        ctx.beginPath();
        ctx.moveTo(x, 0);
        ctx.lineTo(x, height);
        ctx.stroke();
      }
      for (let y = 0; y < height; y += 20) {
        ctx.beginPath();
        ctx.moveTo(0, y);
        ctx.lineTo(width, y);
        ctx.stroke();
      }

      // Draw ECG wave
      ctx.strokeStyle = '#00F2FE';
      ctx.lineWidth = 2.5;
      ctx.beginPath();

      const period = 80;
      offset = (offset + 2) % width;

      for (let x = 0; x < width; x++) {
        const norm = ((x + offset) % period) / period;
        let ySample = 0;
        if (norm >= 0.1 && norm <= 0.18) ySample = Math.sin((norm - 0.1) / 0.08 * Math.PI) * 15;
        else if (norm >= 0.22 && norm <= 0.25) ySample = -15;
        else if (norm >= 0.25 && norm <= 0.3) ySample = Math.sin((norm - 0.25) / 0.05 * Math.PI) * 60;
        else if (norm >= 0.3 && norm <= 0.34) ySample = -20;
        else if (norm >= 0.42 && norm <= 0.58) ySample = Math.sin((norm - 0.42) / 0.16 * Math.PI) * 20;

        const y = height / 2 - ySample;
        if (x === 0) ctx.moveTo(x, y);
        else ctx.lineTo(x, y);
      }
      ctx.stroke();

      animId = requestAnimationFrame(render);
    };

    render();
    return () => cancelAnimationFrame(animId);
  }, [activeTab, vitals.hr]);

  // Handlers
  const handleTriggerSos = () => {
    const payload = `AEGIS|GPS:28.6139,77.2090|HR:${Math.round(vitals.hr)}|SPO2:${Math.round(vitals.spo2)}|RISK:${assessment.score}|THREAT:${assessment.primaryThreat.substring(0, 15)}`;
    const hex = Array.from(payload).map(c => c.charCodeAt(0).toString(16).padStart(2, '0')).join('').toUpperCase();
    setEmergencySmsPayload(hex);
    setDisasterQueue(q => [payload, ...q]);
    alert("Emergency SOS Broadcasted! Hex Encrypted Payload Queued.");
  };

  const handlePurgeVault = () => {
    setAlerts([]);
    setDisasterQueue([]);
    alert("Encrypted Local Room Data Vault Purged Successfully!");
  };

  const navItems = [
    { id: 'dashboard', name: 'Dashboard', icon: Layers },
    { id: 'live_monitor', name: 'Live', icon: Activity },
    { id: 'ai_risk', name: 'AI Risk', icon: Cpu },
    { id: 'history', name: 'History', icon: BarChart3 },
    { id: 'summary', name: 'Summary', icon: FileText },
    { id: 'alerts', name: 'Alerts', icon: Bell },
    { id: 'disaster_mode', name: 'Disaster', icon: AlertTriangle },
    { id: 'privacy', name: 'Privacy', icon: Shield },
    { id: 'device', name: 'Device', icon: Radio },
    { id: 'settings', name: 'Settings', icon: Sliders },
  ];

  return (
    <div className={`min-h-screen ${activeTab === 'disaster_mode' ? 'bg-[#180006]' : 'bg-[#0A0E17]'} text-slate-100 flex flex-col md:flex-row font-sans`}>

      {/* Sidebar Navigation */}
      <aside className="w-full md:w-64 bg-[#131B2E] border-r border-[#2B3A5A] p-4 flex flex-col justify-between">
        <div>
          {/* Logo Header */}
          <div className="flex items-center gap-3 pb-6 border-b border-[#2B3A5A]">
            <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-[#00F2FE] to-[#7F00FF] p-[2px]">
              <div className="w-full h-full bg-[#0A0E17] rounded-[10px] flex items-center justify-center">
                <Shield className="w-5 h-5 text-[#00F2FE]" />
              </div>
            </div>
            <div>
              <h1 className="font-bold text-lg leading-none tracking-tight text-white">AEGIS HEALTH</h1>
              <p className="text-[10px] text-[#00F2FE] font-mono tracking-wider mt-1">SIH26181 QUALCOMM</p>
            </div>
          </div>

          {/* Navigation Items */}
          <nav className="mt-6 space-y-1">
            {navItems.map((item) => {
              const Icon = item.icon;
              const isActive = activeTab === item.id;
              return (
                <button
                  key={item.id}
                  onClick={() => setActiveTab(item.id)}
                  className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl text-sm font-medium transition-all ${
                    isActive
                      ? item.id === 'disaster_mode'
                        ? 'bg-[#FF2A6D] text-white font-bold'
                        : 'bg-[#1C273E] text-[#00F2FE] border-l-4 border-[#00F2FE]'
                      : 'text-slate-400 hover:text-slate-200 hover:bg-[#1A2338]'
                  }`}
                >
                  <Icon className={`w-4 h-4 ${isActive ? (item.id === 'disaster_mode' ? 'text-white' : 'text-[#00F2FE]') : 'text-slate-400'}`} />
                  <span>{item.name}</span>
                </button>
              );
            })}
          </nav>
        </div>

        {/* System Status Footer */}
        <div className="pt-4 border-t border-[#2B3A5A] text-xs text-slate-400 space-y-2">
          <div className="flex items-center justify-between">
            <span className="flex items-center gap-1.5 text-[#00F5D4] font-medium">
              <span className="w-2 h-2 rounded-full bg-[#00F5D4] animate-pulse"></span>
              Qualcomm NPU Edge
            </span>
            <span className="font-mono text-[10px]">3ms</span>
          </div>
          <p className="text-[10px] text-slate-500">Encrypted Vault: Keystore AES-256</p>
        </div>
      </aside>

      {/* Main Screen Content Area */}
      <main className="flex-1 p-4 md:p-8 max-w-7xl mx-auto overflow-y-auto">

        {/* SCREEN 1: DASHBOARD */}
        {activeTab === 'dashboard' && (
          <div className="space-y-6 animate-fadeIn">
            {/* Top Bar */}
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
              <div>
                <span className="text-xs font-mono tracking-widest text-[#00F2FE]">OFFLINE AI GUARDIAN CONTROL</span>
                <h2 className="text-2xl md:text-3xl font-bold">Medical Command Center</h2>
              </div>
              <button
                onClick={handleTriggerSos}
                className="self-start sm:self-auto px-6 py-3 rounded-2xl bg-[#FF2A6D] hover:bg-rose-600 text-white font-bold flex items-center gap-2 shadow-lg shadow-rose-950/50 transition-all active:scale-95"
              >
                <AlertTriangle className="w-5 h-5" />
                <span>EMERGENCY SOS</span>
              </button>
            </div>

            {/* Active Alert Banner if exists */}
            {alerts.length > 0 && (
              <div className="glass-panel p-4 rounded-2xl border-l-4 border-[#FF2A6D] bg-rose-950/20 flex items-start justify-between">
                <div className="flex items-center gap-3">
                  <Bell className="w-6 h-6 text-[#FF2A6D] animate-bounce" />
                  <div>
                    <h4 className="font-bold text-[#FF2A6D]">{alerts[0].title}</h4>
                    <p className="text-sm text-slate-300">{alerts[0].message}</p>
                  </div>
                </div>
                <span className="text-xs font-mono text-slate-400">{alerts[0].timestamp}</span>
              </div>
            )}

            {/* AI Risk Score Banner */}
            <div className="glass-panel p-6 rounded-3xl relative overflow-hidden flex flex-col md:flex-row items-center justify-between gap-6">
              <div className="space-y-2 text-center md:text-left">
                <span className="text-xs font-mono text-slate-400">ON-DEVICE AI RISK ENGINE</span>
                <h3 className="text-xl font-bold">Physiological Threat Level</h3>
                <p className="text-sm text-slate-300 max-w-md">
                  Primary Vector: <strong className="text-[#00F2FE]">{assessment.primaryThreat}</strong>
                </p>
                <div className="flex items-center gap-4 pt-2">
                  <span className="text-xs px-2.5 py-1 rounded-md bg-[#1C273E] text-[#00F5D4]">100% Offline Edge</span>
                  <span className="text-xs text-slate-400">Inference: {assessment.inferenceTime}ms</span>
                </div>
              </div>

              {/* Radial Gauge Visual */}
              <div className="relative w-36 h-36 flex items-center justify-center">
                <svg className="w-full h-full transform -rotate-90">
                  <circle cx="72" cy="72" r="58" stroke="#1C273E" strokeWidth="12" fill="transparent" />
                  <circle
                    cx="72"
                    cy="72"
                    r="58"
                    stroke={assessment.score > 55 ? "#FF2A6D" : assessment.score > 25 ? "#FFB800" : "#00F5D4"}
                    strokeWidth="12"
                    strokeDasharray={364}
                    strokeDashoffset={364 - (364 * assessment.score) / 100}
                    strokeLinecap="round"
                    fill="transparent"
                    className="transition-all duration-700 ease-out"
                  />
                </svg>
                <div className="absolute flex flex-col items-center">
                  <span className="text-3xl font-extrabold">{assessment.score}%</span>
                  <span className={`text-[10px] font-bold ${assessment.score > 55 ? 'text-[#FF2A6D]' : assessment.score > 25 ? 'text-[#FFB800]' : 'text-[#00F5D4]'}`}>
                    {assessment.level}
                  </span>
                </div>
              </div>
            </div>

            {/* 4 Sensor Vitals Rings */}
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
              {/* HR Card */}
              <div className="glass-panel p-5 rounded-2xl space-y-3">
                <div className="flex items-center justify-between text-xs text-slate-400 font-mono">
                  <span>HEART RATE</span>
                  <Heart className="w-4 h-4 text-[#FF2A6D]" />
                </div>
                <div className="flex items-baseline gap-2">
                  <span className="text-3xl font-bold">{Math.round(vitals.hr)}</span>
                  <span className="text-xs text-slate-400">BPM</span>
                </div>
                <div className="text-xs text-[#00F5D4]">Baseline: {baseline.hrMean} BPM</div>
              </div>

              {/* SpO2 Card */}
              <div className="glass-panel p-5 rounded-2xl space-y-3">
                <div className="flex items-center justify-between text-xs text-slate-400 font-mono">
                  <span>SPO₂ SATURATION</span>
                  <Zap className="w-4 h-4 text-[#00F2FE]" />
                </div>
                <div className="flex items-baseline gap-2">
                  <span className="text-3xl font-bold">{Math.round(vitals.spo2)}</span>
                  <span className="text-xs text-slate-400">%</span>
                </div>
                <div className="text-xs text-[#00F2FE]">Baseline: {baseline.spo2Baseline}%</div>
              </div>

              {/* Core Temp Card */}
              <div className="glass-panel p-5 rounded-2xl space-y-3">
                <div className="flex items-center justify-between text-xs text-slate-400 font-mono">
                  <span>CORE TEMP</span>
                  <Flame className="w-4 h-4 text-[#FFB800]" />
                </div>
                <div className="flex items-baseline gap-2">
                  <span className="text-3xl font-bold">{vitals.temp.toFixed(1)}</span>
                  <span className="text-xs text-slate-400">°C</span>
                </div>
                <div className="text-xs text-[#FFB800]">Heat Index: {(vitals.ambientTemp + 2).toFixed(1)}°C</div>
              </div>

              {/* AQI Card */}
              <div className="glass-panel p-5 rounded-2xl space-y-3">
                <div className="flex items-center justify-between text-xs text-slate-400 font-mono">
                  <span>AIR QUALITY AQI</span>
                  <Wind className="w-4 h-4 text-[#7F00FF]" />
                </div>
                <div className="flex items-baseline gap-2">
                  <span className="text-3xl font-bold">{Math.round(vitals.aqi)}</span>
                  <span className="text-xs text-slate-400">PM2.5</span>
                </div>
                <div className="text-xs text-[#7F00FF]">{vitals.aqi > 100 ? 'Unhealthy' : 'Good Quality'}</div>
              </div>
            </div>

            {/* Baseline Status */}
            <div className="glass-panel p-5 rounded-2xl flex items-center justify-between">
              <div>
                <h4 className="font-bold text-sm">Personalized Adaptive Baseline</h4>
                <p className="text-xs text-slate-400">Confidence: {baseline.confidence}% ({baseline.samples} rolling samples)</p>
              </div>
              <span className="text-xs font-mono text-[#00F5D4] bg-[#00F5D4]/10 px-3 py-1.5 rounded-lg border border-[#00F5D4]/30">
                ACTIVE PIPELINE
              </span>
            </div>
          </div>
        )}

        {/* SCREEN 2: LIVE MONITOR */}
        {activeTab === 'live_monitor' && (
          <div className="space-y-6 animate-fadeIn">
            <div>
              <span className="text-xs font-mono tracking-widest text-[#00F2FE]">HIGH FREQUENCY OSCILLOSCOPE</span>
              <h2 className="text-2xl font-bold">Live Dynamic Telemetry Waveform</h2>
            </div>

            {/* Canvas PPG/ECG Wave */}
            <div className="glass-panel p-6 rounded-3xl space-y-4">
              <div className="flex items-center justify-between text-xs font-mono text-slate-400">
                <span>PPG / ECG SENSOR TRACE</span>
                <span className="text-[#00F5D4]">SQI: {vitals.sqi.toFixed(1)}%</span>
              </div>
              <canvas ref={canvasRef} width={800} height={180} className="w-full h-44 bg-[#0A0E17] rounded-xl border border-[#2B3A5A]" />
            </div>

            {/* 3-Axis Vector */}
            <div className="glass-panel p-6 rounded-3xl space-y-4">
              <h3 className="text-sm font-bold text-slate-300 tracking-wider font-mono">3-AXIS ACCELEROMETER MOTION VECTOR</h3>
              <div className="grid grid-cols-1 sm:grid-cols-4 gap-4">
                <div className="bg-[#0A0E17] p-4 rounded-xl border border-[#2B3A5A]">
                  <span className="text-xs text-slate-400 block">X-Axis</span>
                  <span className="text-xl font-bold text-[#00F2FE]">{vitals.accelX.toFixed(3)}G</span>
                </div>
                <div className="bg-[#0A0E17] p-4 rounded-xl border border-[#2B3A5A]">
                  <span className="text-xs text-slate-400 block">Y-Axis</span>
                  <span className="text-xl font-bold text-[#00F5D4]">{vitals.accelY.toFixed(3)}G</span>
                </div>
                <div className="bg-[#0A0E17] p-4 rounded-xl border border-[#2B3A5A]">
                  <span className="text-xs text-slate-400 block">Z-Axis</span>
                  <span className="text-xl font-bold text-[#FFB800]">{vitals.accelZ.toFixed(3)}G</span>
                </div>
                <div className="bg-[#0A0E17] p-4 rounded-xl border border-[#2B3A5A]">
                  <span className="text-xs text-slate-400 block">Magnitude</span>
                  <span className="text-xl font-bold text-[#FF2A6D]">
                    {Math.sqrt(vitals.accelX**2 + vitals.accelY**2 + vitals.accelZ**2).toFixed(2)}G
                  </span>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* SCREEN 3: AI RISK & EXPLAINABLE AI */}
        {activeTab === 'ai_risk' && (
          <div className="space-y-6 animate-fadeIn">
            <div>
              <span className="text-xs font-mono tracking-widest text-[#00F2FE]">EXPLAINABLE AI ENGINE (XAI)</span>
              <h2 className="text-2xl font-bold">Qualcomm Edge Multi-Vector Risk Fusion</h2>
            </div>

            <div className="glass-panel p-6 rounded-3xl space-y-4">
              <h3 className="font-bold text-sm text-slate-400 font-mono">FEATURE IMPORTANCE & ANOMALY BREAKDOWN</h3>

              {assessment.factors.length === 0 ? (
                <div className="p-4 rounded-2xl bg-[#00F5D4]/10 border border-[#00F5D4]/30 text-[#00F5D4] text-sm">
                  ✓ All physiological telemetry parameters are operating within safe baseline parameters.
                </div>
              ) : (
                <div className="space-y-3">
                  {assessment.factors.map((f, i) => (
                    <div key={i} className="p-4 rounded-2xl bg-[#0A0E17] border border-[#2B3A5A] space-y-2">
                      <div className="flex items-center justify-between">
                        <span className="font-bold text-white">{f.name}</span>
                        <span className="text-sm font-bold text-[#FF2A6D]">+{f.impact}% Risk</span>
                      </div>
                      <p className="text-xs text-slate-300">{f.status}</p>
                      <p className="text-[11px] text-slate-500 font-mono">Shift: {f.comparison}</p>
                      <div className="w-full h-1.5 bg-[#1C273E] rounded-full overflow-hidden">
                        <div className="h-full bg-[#FF2A6D]" style={{ width: `${(f.impact / 50) * 100}%` }}></div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        )}

        {/* SCREEN 4: HISTORY */}
        {activeTab === 'history' && (
          <div className="space-y-6 animate-fadeIn">
            <div className="flex items-center justify-between">
              <div>
                <span className="text-xs font-mono text-[#00F2FE]">ENCRYPTED LOCAL STORAGE</span>
                <h2 className="text-2xl font-bold">Historical Vitals Audit</h2>
              </div>
              <button onClick={() => alert("Encrypted Medical CSV Exported!")} className="px-4 py-2 rounded-xl bg-[#1C273E] text-[#00F2FE] text-xs font-bold border border-[#00F2FE]/30">
                Export Encrypted Log
              </button>
            </div>

            <div className="glass-panel p-6 rounded-3xl space-y-4">
              <h3 className="text-sm font-mono text-slate-400">7-DAY HEART RATE & SPO₂ TRENDS</h3>
              <div className="h-44 flex items-end justify-between gap-2 pt-8">
                {['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'].map((day, idx) => {
                  const heights = [70, 68, 75, 72, 70, 78, 72];
                  return (
                    <div key={day} className="flex-1 flex flex-col items-center gap-2">
                      <div className="w-full bg-[#00F2FE] rounded-t-lg transition-all" style={{ height: `${heights[idx]}%` }}></div>
                      <span className="text-xs text-slate-400">{day}</span>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>
        )}

        {/* SCREEN 5: SUMMARY */}
        {activeTab === 'summary' && (
          <div className="space-y-6 animate-fadeIn">
            <div>
              <span className="text-xs font-mono text-[#00F2FE]">DAILY HEALTH REPORT</span>
              <h2 className="text-2xl font-bold">Guardian Health & Strain Summary</h2>
            </div>

            <div className="glass-panel p-6 rounded-3xl text-center space-y-2">
              <span className="text-xs font-mono text-slate-400">DAILY HEALTH INDEX</span>
              <div className="text-5xl font-extrabold text-[#00F5D4]">94 / 100</div>
              <p className="text-sm text-slate-300">Optimal Physiological & Circadian Stability</p>
            </div>
          </div>
        )}

        {/* SCREEN 6: ALERTS */}
        {activeTab === 'alerts' && (
          <div className="space-y-6 animate-fadeIn">
            <div>
              <span className="text-xs font-mono text-[#00F2FE]">REAL-TIME AUDIT LOG</span>
              <h2 className="text-2xl font-bold">Triggered Emergency Alerts</h2>
            </div>

            {alerts.length === 0 ? (
              <div className="glass-panel p-6 rounded-3xl text-slate-400 text-sm">
                No active or historical alerts logged.
              </div>
            ) : (
              <div className="space-y-3">
                {alerts.map(a => (
                  <div key={a.id} className="glass-panel p-4 rounded-2xl border-l-4 border-[#FF2A6D] flex justify-between items-center">
                    <div>
                      <h4 className="font-bold text-[#FF2A6D]">{a.title}</h4>
                      <p className="text-sm text-slate-300">{a.message}</p>
                    </div>
                    <span className="text-xs font-mono text-slate-400">{a.timestamp}</span>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {/* SCREEN 7: DISASTER MODE */}
        {activeTab === 'disaster_mode' && (
          <div className="space-y-6 animate-fadeIn">
            <div className="flex items-center justify-between">
              <div>
                <span className="text-xs font-mono text-[#FF2A6D]">ZERO INTERNET PROTOCOL</span>
                <h2 className="text-2xl font-bold text-white">Disaster Emergency Beacon</h2>
              </div>
              <span className="px-3 py-1 bg-[#FF2A6D] text-white text-xs font-bold rounded-lg">OFFLINE</span>
            </div>

            {/* Encrypted SMS Payload Box */}
            <div className="glass-panel-disaster p-6 rounded-3xl space-y-4">
              <h3 className="text-xs font-mono text-[#FF2A6D]">COMPACT SMS ENCODED HEX PROTOCOL</h3>
              <div className="p-4 bg-black/60 rounded-xl font-mono text-xs text-[#FFB800] break-all border border-[#FF2A6D]/30">
                {emergencySmsPayload || "41454749537C4750533A32382E363133392C37372E323039307C48523A37327C53504F323A39387C5249534B3A3132"}
              </div>
              <button onClick={handleTriggerSos} className="px-6 py-3 rounded-xl bg-[#FF2A6D] text-white font-bold text-sm">
                Generate & Broadcast SOS SMS Payload
              </button>
            </div>
          </div>
        )}

        {/* SCREEN 8: PRIVACY & CONSENT */}
        {activeTab === 'privacy' && (
          <div className="space-y-6 animate-fadeIn">
            <div>
              <span className="text-xs font-mono text-[#00F2FE]">SECURITY & CONSENT MATRIX</span>
              <h2 className="text-2xl font-bold">Granular Sensor & Vault Privacy</h2>
            </div>

            <div className="glass-panel p-6 rounded-3xl space-y-4">
              <div className="flex items-center justify-between pb-4 border-b border-[#2B3A5A]">
                <div>
                  <h4 className="font-bold text-white">Android Keystore Encryption</h4>
                  <p className="text-xs text-slate-400">AES-256 GCM Encrypted Room SQLCipher Vault</p>
                </div>
                <span className="text-xs font-mono text-[#00F5D4] bg-[#00F5D4]/10 px-3 py-1.5 rounded-lg border border-[#00F5D4]/30">
                  ENCRYPTED
                </span>
              </div>

              <div className="space-y-3">
                <div className="flex items-center justify-between">
                  <span className="text-sm">Heart Rate PPG Sensor Access</span>
                  <input type="checkbox" checked={privacy.hrConsent} onChange={e => setPrivacy({...privacy, hrConsent: e.target.checked})} className="toggle" />
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-sm">SpO₂ Oximeter Sensor Access</span>
                  <input type="checkbox" checked={privacy.spo2Consent} onChange={e => setPrivacy({...privacy, spo2Consent: e.target.checked})} className="toggle" />
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-sm">Strict Zero-Cloud Mode (100% Outbound Block)</span>
                  <input type="checkbox" checked={privacy.zeroCloud} onChange={e => setPrivacy({...privacy, zeroCloud: e.target.checked})} className="toggle" />
                </div>
              </div>

              <button onClick={handlePurgeVault} className="w-full mt-4 py-3 rounded-xl bg-rose-950/40 text-[#FF2A6D] border border-[#FF2A6D]/40 font-bold text-xs">
                Purge All Local Encrypted Data Vault
              </button>
            </div>
          </div>
        )}

        {/* SCREEN 9: DEVICE & SIMULATION CONTROL */}
        {activeTab === 'device' && (
          <div className="space-y-6 animate-fadeIn">
            <div>
              <span className="text-xs font-mono text-[#00F2FE]">BLE SIMULATOR & SCENARIO CONTROL</span>
              <h2 className="text-2xl font-bold">Qualcomm Telemetry Anomaly Injector</h2>
            </div>

            <div className="glass-panel p-6 rounded-3xl space-y-3">
              {[
                { id: 'NORMAL', label: 'Normal Resting Baseline', desc: 'HR 72, SpO2 98%, Temp 36.6°C, AQI 42' },
                { id: 'ARRHYTHMIA', label: 'Arrhythmia / Tachycardia Surge', desc: 'Rapid pulse spike to 148 BPM' },
                { id: 'FALL', label: 'Sudden High-G Impact / Fall', desc: 'Acceleration spike to 3.85G' },
                { id: 'HEAT', label: 'Heat Stroke Thermal Stress', desc: 'Ambient 42.5°C, core temp surge' },
                { id: 'HYPOXIA', label: 'Hypoxia / SpO2 Drop', desc: 'Oxygen dip to 84% + high AQI' },
              ].map((s) => (
                <button
                  key={s.id}
                  onClick={() => setScenario(s.id)}
                  className={`w-full p-4 rounded-2xl text-left border transition-all ${
                    scenario === s.id
                      ? 'bg-[#1C273E] border-[#00F2FE] text-white font-bold'
                      : 'bg-[#0A0E17] border-[#2B3A5A] text-slate-400 hover:border-slate-500'
                  }`}
                >
                  <div className="flex items-center justify-between">
                    <span className="text-sm font-bold">{s.label}</span>
                    {scenario === s.id && <span className="text-xs text-[#00F2FE]">ACTIVE SCENARIO</span>}
                  </div>
                  <p className="text-xs text-slate-400 mt-1">{s.desc}</p>
                </button>
              ))}
            </div>
          </div>
        )}

        {/* SCREEN 10: SETTINGS */}
        {activeTab === 'settings' && (
          <div className="space-y-6 animate-fadeIn">
            <div>
              <span className="text-xs font-mono text-[#00F2FE]">SYSTEM CONFIGURATION</span>
              <h2 className="text-2xl font-bold">Qualcomm Guardian Settings</h2>
            </div>

            <div className="glass-panel p-6 rounded-3xl space-y-4">
              <h3 className="font-bold text-sm text-[#00F2FE]">QUALCOMM SNAPDRAGON EDGE NPU</h3>
              <p className="text-sm text-slate-300">Model: aegis_health_quantized_v2.tflite (Hexagon Vector Extensions Enabled)</p>
            </div>
          </div>
        )}

      </main>
    </div>
  );
}

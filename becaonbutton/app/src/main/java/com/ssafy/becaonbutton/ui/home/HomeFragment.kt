package com.ssafy.becaonbutton.ui.home

import android.Manifest
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ssafy.becaonbutton.R
import com.ssafy.becaonbutton.databinding.FragmentHomeBinding
import com.ssafy.becaonbutton.ui.home.Constants.BEACON_DISTANCE
import com.ssafy.becaonbutton.ui.home.Constants.PERMISSION_REQUEST_CODE
import com.ssafy.becaonbutton.ui.home.Constants.RUNTIME_PERMISSIONS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.MonitorNotifier
import org.altbeacon.beacon.RangeNotifier
import org.altbeacon.beacon.Region
private const val TAG = "태그"
class HomeFragment : Fragment() {

    private lateinit var beaconButton: Button

    // beacon
    private lateinit var beaconManager: BeaconManager
    private lateinit var checkPermission: CheckPermission
    private lateinit var bleScanner: BluetoothLeScanner
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private val runtimePermissions = Constants.RUNTIME_PERMISSIONS
    private var eventPopUpAble = true
    private val region = Constants.REGIONS

    private val requestBluetoothActivationLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            bleScanner = bluetoothAdapter.bluetoothLeScanner // 문제의 코드
        } else {
            Toast.makeText(activity, "블루투스 활성화가 거부되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private val bluetoothManager by lazy {
        requireActivity().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        beaconButton = view.findViewById(R.id.beacon_button)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bluetoothAdapter = bluetoothManager.adapter
        checkPermission = CheckPermission(requireActivity())
        initBeacon()
    }

    private fun initBeacon() {
        //BeaconManager 지정
        beaconManager = BeaconManager.getInstanceForApplication(requireActivity())
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
        bluetoothAdapter = bluetoothManager.adapter
        checkAllPermission()
    }


    // Register a result launcher for permission request
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
            permissions[Manifest.permission.BLUETOOTH_SCAN] == true &&
            permissions[Manifest.permission.BLUETOOTH_ADVERTISE] == true &&
            permissions[Manifest.permission.BLUETOOTH_CONNECT] == true) {
            // Permission granted
            turnOnBluetooth()
            startScan()
        } else {
            Toast.makeText(activity, "권한 요청이 거부되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAllPermission() {
        if (!checkPermission()) {
            // Request permissions
            requestPermissions()
        } else {
            // Permissions are already granted
            turnOnBluetooth()
            startScan()
        }
    }

    private fun checkPermission(): Boolean {
        // Check if all permissions are already granted
        val permissionResults = RUNTIME_PERMISSIONS.map {
            ActivityCompat.checkSelfPermission(requireContext(), it)
        }
        return permissionResults.all { it == PackageManager.PERMISSION_GRANTED }
    }

    private fun requestPermissions() {
        requestPermissionLauncher.launch(RUNTIME_PERMISSIONS)
    }

//    private fun checkAllPermission() {
//        if (!checkPermission.runtimeCheckPermission(activity, *runtimePermissions)) {
//            Log.d(TAG, "initBeacon: 권한없음")
//            ActivityCompat.requestPermissions(requireActivity(), runtimePermissions, PERMISSION_REQUEST_CODE)
//        } else { //이미 전체 권한이 있는 경우
//            turnOnBluetooth()
//            startScan()
//        }
//    }

    // beacon 초기화
    private fun startScan() {
        var monitorNotifier: MonitorNotifier = object : MonitorNotifier {
            override fun didEnterRegion(region: Region) {
                Handler(Looper.getMainLooper()).post{
                    Toast.makeText(activity,"비컨 발견",Toast.LENGTH_SHORT).show()
                }
            }

            override fun didExitRegion(region: Region) { //발견 못함.
            }

            override fun didDetermineStateForRegion(state: Int, region: Region) { //상태변경
            }
        }

        var rangeNotifier = RangeNotifier { beacons, _ ->
            beacons?.run{
                if (isNotEmpty()) {
                    forEach { beacon ->
                        // 사정거리 내에 있을 경우 이벤트 표시 다이얼로그 팝업
                        if (beacon.distance <= BEACON_DISTANCE && eventPopUpAble == true) {
                            Log.d(TAG, "didRangeBeaconsInRegion: distance 이내.")
                            beaconButton.isVisible = true
                            eventPopUpAble = false
                        } else {
                            Log.d(TAG, "didRangeBeaconsInRegion: distance 이외.")
                        }
                        Log.d( TAG,"distance: " + beacon.distance + " id:" + beacon.id1 + "/" + beacon.id2 + "/" + beacon.id3)
                    }
                }
                if (isEmpty()) {
                    Log.d(TAG, "didRangeBeaconsInRegion: 비컨을 찾을 수 없습니다.")
                }
            }
        }

        // 리전에 비컨이 있는지 없는지..정보를 받는 클래스 지정
        beaconManager.addMonitorNotifier(monitorNotifier) // 지정한 DISTANCE 안에 있는지 확인
        beaconManager.startMonitoring(region)

        //detacting되는 해당 region의 beacon정보를 받는 클래스 지정.
        beaconManager.addRangeNotifier(rangeNotifier) // 비컨이 얼마나 떨어진 '거리' 에 있는지 알려줌
        beaconManager.startRangingBeacons(region)
    }

    // 블루투스 기능 꺼져있을 때
    private fun turnOnBluetooth() {
        if (bluetoothAdapter == null || !bluetoothAdapter!!.isEnabled) {
            Toast.makeText(activity, "블루투스 기능을 확인해 주세요.", Toast.LENGTH_SHORT).show()
            requestBluetoothActivation()
        } else {
            bleScanner = bluetoothAdapter.bluetoothLeScanner // 문제의 코드
        }
    }

    private fun requestBluetoothActivation() {
        val bleIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        requestBluetoothActivationLauncher.launch(bleIntent)
    }
}
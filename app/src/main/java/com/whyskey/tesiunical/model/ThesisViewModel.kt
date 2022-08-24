package com.whyskey.tesiunical.model

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.whyskey.tesiunical.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ThesisViewModel : ViewModel(){

    private var _auth: FirebaseAuth = Firebase.auth
    val auth = _auth

    private var _storage = Firebase.storage.reference
    val storage = _storage

    private var _user = auth.currentUser
    val user = _user

    private val _compilationThesis = mutableStateOf<List<Thesis>>(emptyList())
    val compilationThesis: State<List<Thesis>>
        get() = _compilationThesis

    private val _applicationThesis = mutableStateOf<List<Thesis>>(emptyList())
    val applicationThesis: State<List<Thesis>>
        get() = _applicationThesis

    private val _researchThesis = mutableStateOf<List<Thesis>>(emptyList())
    val researchThesis: State<List<Thesis>>
        get() = _researchThesis

    private val _corporateThesis = mutableStateOf<List<Thesis>>(emptyList())
    val corporateThesis: State<List<Thesis>>
        get() = _corporateThesis

    private val _erasmusThesis = mutableStateOf<List<Thesis>>(emptyList())
    val erasmusThesis: State<List<Thesis>>
        get() = _erasmusThesis

    private val _thesis = mutableStateOf(Thesis())
    val thesis: State<Thesis>
        get() = _thesis

    private val _userData = mutableStateOf(Account())
    val userData: State<Account>
        get() = _userData

    private val _marchSession = mutableStateOf(Session())
    val marchSession: State<Session>
        get() = _marchSession

    private val _julySession = mutableStateOf(Session())
    val julySession: State<Session>
        get() = _julySession

    private val _septemberSession = mutableStateOf(Session())
    val septemberSession: State<Session>
        get() = _septemberSession

    private val _decemberSession = mutableStateOf(Session())
    val decemberSession: State<Session>
        get() = _decemberSession

    private val _userImage = mutableStateOf<Uri?>(null)
    val userImage: State<Uri?>
        get() = _userImage

    private val _accounts = mutableStateOf<List<Account>>(emptyList())
    val accounts: State<List<Account>>
        get() = _accounts

    private val _requests = mutableStateOf<List<Request>>(emptyList())
    val requests: State<List<Request>>
        get() = _requests

    init {
        if(user != null){
            getAllData()
        }
    }

    fun returnThesis(){
        getThesis()
    }

    private fun getThesis(){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(_userData.value.id).collection("thesis").addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e)
                    return@addSnapshotListener
                }
                if( value != null ) {
                    val thesis = value.toObjects<Thesis>()
                    val temp = thesis[0]
                    _thesis.value = temp
                }
            }
        }
    }

    fun setExams(exams: String){
        updateExams(exams)
    }

    private fun updateExams(exams:String){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(_userData.value.id)
                .update(mapOf(
                    "exams" to exams
                ))
        }
    }

    fun changeRequest(id: String, idStudent: String, idThesis: String,session: Int,accepted: Boolean, context: Context?){
        viewModelScope.launch{
            Firebase.firestore.collection("account").document(idStudent).collection("thesis").document(idThesis)
                .get().addOnSuccessListener {
                    if(it != null) {
                        val thesis = it.toObject(Thesis::class.java)

                        if(accepted){

                            val temp = when(session){
                                0 -> "march"
                                1 -> "july"
                                2 -> "september"
                                else -> "december"
                            }

                            val temp2 = when(thesis!!.type){
                                0 -> "applicative"
                                1 -> "compilation"
                                2 -> "corporate"
                                3 -> "erasmus"
                                else -> "research"
                            }

                            Firebase.firestore.collection("account").document(_userData.value.id).collection("sessions").document(temp)
                                .get().addOnSuccessListener { document ->

                                    val session = document.toObject<Session>()!!
                                    val temp3 = when(temp2){
                                        "applicative" -> session.applicative.values
                                        "compilation" -> session.compilation.values
                                        "corporate" -> session.corporate.values
                                        "erasmus" -> session.erasmus.values
                                        else ->  session.research.values
                                    }

                                    if(_userData.value.hasLimit && temp3.toList()[0] >= temp3.toList()[1]){
                                        val text = "Limite raggiunto o superato!"
                                        val duration = Toast.LENGTH_SHORT
                                        val toast = Toast.makeText(context,text, duration)
                                        toast.show()
                                        Log.d("LIMIT","Limite Superato")
                                    } else {
                                        Firebase.firestore.collection("account").document(_userData.value.id).collection("sessions").document(temp)
                                            .update(
                                                "${temp2}.current",FieldValue.increment(1)
                                            )

                                        Firebase.firestore.collection("requests").document(id)
                                            .update(mapOf("accepted" to true))

                                        Firebase.firestore.collection("account").document(idStudent)
                                            .update(mapOf("hasThesis" to true))
                                    }
                                }

                        } else {
                            Firebase.firestore.collection("requests").document(id)
                                .delete()
                            sendThesis(idStudent,idThesis,thesis!!)

                        }
                    }
                }
        }
    }

    private fun sendThesis(account: String, idThesis:String, thesis: Thesis){
        viewModelScope.launch {
            Firebase.firestore.runTransaction {
                Firebase.firestore.collection("account").document(_userData.value.id).collection("thesis").document(idThesis)
                    .set(thesis)
                Firebase.firestore.collection("account").document(account).collection("thesis").document(idThesis)
                    .delete()
                null
            }.addOnSuccessListener { Log.d("TAG", "Transaction success!") }
                .addOnFailureListener { e -> Log.w("TAG", "Transaction failure.", e) }
        }
    }

    fun getImage(profile:Account){
        retrieveImage(profile)
    }

    fun getAllData(){
        getUserData(Firebase.auth.currentUser?.uid)
        getImage(_userData.value)
    }

    fun getAccountsByType(type: Boolean){
        getAccounts(type)
    }

    private fun getAccounts(type: Boolean) {
        viewModelScope.launch {
            if(type){
                Firebase.firestore.collection("account").whereEqualTo("isProfessor",type)
                    .addSnapshotListener { value, e ->
                        if(value != null){
                            if (e != null) {
                                Log.w("TAG", "Listen failed.", e)
                                return@addSnapshotListener
                            }
                            val accounts = ArrayList<Account>()
                            val documents = value.documents
                            documents.forEach {
                                val temp = it.toObject(Account::class.java)
                                if(temp != null){
                                    temp.id = it.id
                                    accounts.add(temp)
                                }
                            }
                            _accounts.value = accounts
                            val requests = ArrayList<Request>()
                            accounts.forEach {
                                requests.add(
                                    Request(
                                        id_professor = it.id,
                                        name = it.name,
                                        image = it.image
                                    )
                                )
                            }
                            _requests.value = requests
                        }
                    }
            } else {
                Firebase.firestore.collection("requests").whereEqualTo("id_professor",_userData.value.id)
                    .addSnapshotListener { value, e ->
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e)
                            return@addSnapshotListener
                        }

                        if(value != null){
                            val requests = ArrayList<Request>()
                            val documents = value.documents
                            documents.forEach {
                                val temp = it.toObject(Request::class.java)
                                if(temp != null){
                                    temp.id = it.id
                                    requests.add(temp)
                                }
                            }
                            _requests.value = requests
                        }
                    }
            }
        }
    }

    private fun retrieveImage(profile: Account){
        val localFile: File = File.createTempFile("localFile", ".jpg")
        viewModelScope.launch {
            storage.child("images/${profile.id}").getFile(localFile).addOnSuccessListener {

                val uri: Uri = localFile.absolutePath.toUri()
                if(profile.id == _userData.value.id){
                    _userImage.value = uri
                } else {
                    profile.image = uri.toString()
                }

            }
        }
    }

    fun removeThesis(thesis: String){
        deleteThesis(thesis)
    }

    private fun deleteThesis(thesis: String){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(_userData.value.id).collection("thesis").document(thesis)
                .delete()
                .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }
        }
    }

    fun changeName(name:String){
        setName(name)
        _showOptionNameDialog.value = false
    }

    private fun setName(name:String){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(_userData.value.id)
                .update(mapOf(
                    "name" to name
                    )
                )
        }
    }

    fun changeEmail(email:String){
        setEmail(email)
        _showOptionEmailDialog.value = false
    }

    private fun setEmail(email:String){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(_userData.value.id)
                .update(mapOf(
                    "email" to email
                ))
        }
    }

    fun changeLimit(idList: String, applicative: String, compilation: String, corporate: String, erasmus: String, research: String){
        val temp = when(idList){
            "0" -> "march"
            "1" -> "july"
            "2" -> "september"
            else -> "december"
        }
        setLimit(temp,applicative.toInt(),compilation.toInt(),corporate.toInt(),erasmus.toInt(),research.toInt())
        onOptionDialogConfirm()
    }

    private fun setLimit(idList: String, applicative: Int, compilation: Int, corporate: Int, erasmus: Int, research: Int){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(_userData.value.id).collection("sessions").document(idList)
                .update(mapOf(
                    "applicative.max" to applicative,
                    "compilation.max" to compilation,
                    "corporate.max" to corporate,
                    "erasmus.max" to erasmus,
                    "research.max" to research
                )
                )
        }
    }

    fun changeWebSite(webSite:String){
        setWebSite(webSite)
        _showOptionWebDialog.value = false
    }

    private fun setWebSite(webSite:String){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(_userData.value.id)
                .update(mapOf(
                    "web_site" to webSite
                    )
                )
        }
    }

    fun getThesis(id:String,type: Int){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(id).collection("thesis").whereEqualTo("type",type)
                .addSnapshotListener { value, e ->
                    if(value != null) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e)
                            return@addSnapshotListener
                        }

                        val thesis = ArrayList<Thesis>()
                        val documents = value.documents
                        documents.forEach {
                            val temp = it.toObject(Thesis::class.java)
                            if(temp != null){
                                temp.id = it.id
                                temp.id_professor = id
                                thesis.add(temp)
                            }
                        }
                        when(type){
                            0 -> _compilationThesis.value = thesis
                            1 -> _applicationThesis.value = thesis
                            2 -> _researchThesis.value = thesis
                            3 -> _corporateThesis.value = thesis
                            4 -> _erasmusThesis.value = thesis
                        }
                    }
            }
        }
    }

    private fun resetAnalytics(month: String){
        when(month){
            "01" -> reset("december")
            "04" -> reset("march")
            "08" -> reset("july")
            else -> reset("september")
        }
    }

    private fun reset(month: String){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(_userData.value.id).collection("sessions").document(month)
                .update(mapOf(
                    "applicative.current" to 0,
                    "compilation.current" to 0,
                    "corporate.current" to 0,
                    "erasmus.current" to 0,
                    "research.current" to 0
                    )
                )
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getUserData(user: String?){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(user!!)
                .addSnapshotListener { value, _ ->
                if(value != null) {
                    _userData.value = value.toObject<Account>()!!
                    _userData.value.id = user
                    if(value.data!!.getValue("isProfessor") == false){
                        _userData.value.isProfessor = false
                    }

                    val dateFormat: DateFormat = SimpleDateFormat("MM")
                    val dateFormatDay: DateFormat = SimpleDateFormat("dd")
                    val date = Date()
                    val month = dateFormat.format((date))
                    val day = dateFormatDay.format((date))
                    if(day == "01" && (month == "01" || month == "04" ||month == "08" || month == "10")){
                        resetAnalytics(month)
                    }
                }
            }
        }
    }

    fun updateHasLimit(value: Boolean){
        sendUpdate(value)
    }

    private fun sendUpdate(value: Boolean){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(_userData.value.id)
                .update(mapOf("hasLimit" to value))
        }
    }

    fun session(idList: Int){
        getSession(idList)
    }

    private fun getSession(idList: Int){
        val temp = when(idList){
            0 -> "march"
            1 -> "july"
            2 -> "september"
            else -> "december"
        }
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(_userData.value.id).collection("sessions").document(temp)
                .addSnapshotListener { value, _ ->
                    if(value != null){
                        when(idList) {
                            0 -> _marchSession.value = value.toObject(Session::class.java)!!
                            1 -> _julySession.value = value.toObject(Session::class.java)!!
                            2 -> _septemberSession.value = value.toObject(Session::class.java)!!
                            else -> _decemberSession.value = value.toObject(Session::class.java)!!
                        }
                    }
                }
        }
    }

    fun addNewRequest(idStudent: String, idProfessor: String, id_thesis: String, name: String, session: Int, thesisTitle: String,email:String,thesis: Thesis){
        val newRequest = getNewRequest(idStudent,idProfessor,id_thesis,name,session,email,thesisTitle)
        insertRequest(newRequest)
        Firebase.firestore.collection("account").document(idProfessor).collection("thesis").document(id_thesis).get()
            .addOnSuccessListener {
                if(it != null){
                    val thesis = it.toObject(Thesis::class.java)
                    if (thesis != null) {
                        sendThesis(idProfessor, id_thesis,thesis)
                    }
                }
            }
    }

    private fun getNewRequest(idStudent: String, idProfessor: String, idThesis: String,name: String, session: Int, email:String,thesisTitle: String): Request{
        return Request(
            id_student = idStudent,
            id_professor = idProfessor,
            id_thesis = idThesis,
            name = name,
            session = session,
            thesis = thesisTitle,
            email = email,
            accepted = false
        )
    }

    private fun insertRequest(request: Request){
        viewModelScope.launch {
            Firebase.firestore.collection("requests").add(request)
        }
    }

    fun addNewThesis(thesisName: String, thesisType: String,thesisDescription: String) {
        val temp = when(thesisType){
            "Tesi Compilativa" -> 0
            "Tesi Applicativa" -> 1
            else -> 2
        }
        val newThesis = getNewThesisEntry(thesisName, temp, thesisDescription)
        insertThesis(newThesis)
        onDialogConfirm()
    }

    private fun getNewThesisEntry(thesisName: String, thesisType: Int, thesisDescription: String): Thesis {
        return Thesis(
            title = thesisName,
            type = thesisType,
            description = thesisDescription,
            id_professor = userData.value.id
        )
    }

    private fun insertThesis(thesis: Thesis) {
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(thesis.id_professor).collection("thesis").add(thesis)
        }
    }

    //Add Thesis Dialog
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    //Option Dialog
    private val _showOptionNameDialog = MutableStateFlow(false)
    val showOptionNameDialog: StateFlow<Boolean> = _showOptionNameDialog.asStateFlow()

    private val _showOptionEmailDialog = MutableStateFlow(false)
    val showOptionEmailDialog: StateFlow<Boolean> = _showOptionEmailDialog.asStateFlow()

    private val _showOptionWebDialog = MutableStateFlow(false)
    val showOptionWebDialog: StateFlow<Boolean> = _showOptionWebDialog.asStateFlow()

    private val _showLimitDialog = MutableStateFlow(false)
    val showLimitDialog: StateFlow<Boolean> = _showLimitDialog.asStateFlow()

    fun onOpenDialogClicked() {
        _showDialog.value = true
    }

    private fun onDialogConfirm() {
        _showDialog.value = false
    }

    fun onDialogDismiss() {
        _showDialog.value = false
    }

    fun onOptionDialogClicked(dialog: Int) {
        when(dialog){
            0 -> _showOptionNameDialog.value = true
            1 -> _showOptionEmailDialog.value = true
            2 -> _showOptionWebDialog.value = true
            3 -> _showLimitDialog.value = true
        }
    }

    fun onOptionDialogConfirm() {
        _showOptionNameDialog.value = false
        _showOptionEmailDialog.value = false
        _showOptionWebDialog.value = false
        _showLimitDialog.value = false
    }
}
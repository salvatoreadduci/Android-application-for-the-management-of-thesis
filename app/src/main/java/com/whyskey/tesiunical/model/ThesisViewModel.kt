package com.whyskey.tesiunical.model

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.whyskey.tesiunical.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    private val _images = mutableStateOf<Map<String,String>>(mutableMapOf())
    val images: State<Map<String,String>>
        get() = _images

    private val _thesis = mutableStateOf<List<Thesis>>(emptyList())
    val thesis: State<List<Thesis>>
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

    private val _visitedAccount = mutableStateOf(Account())
    val visitedAccount: State<Account>
        get() =_visitedAccount

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

    fun addNewAccount(name: String, email: String, isProfessor: Boolean){
        val account = getNewAccountEntry(name,email)
        registerAccount(account,isProfessor)
        getAllData()
    }

    private fun getNewAccountEntry(name: String, email: String): Account{
        return Account(name, email)
    }

    private fun registerAccount(account: Account, isProfessor: Boolean){
        viewModelScope.launch {
            val id = Firebase.auth.currentUser?.uid
            if(id != null){
                Firebase.firestore.collection("account").document(id)
                    .set(account)

                Firebase.firestore.collection("account").document(id)
                    .update(mapOf("isProfessor" to isProfessor))

                if(isProfessor){
                    val limit = hashMapOf(
                        "current" to 0,
                        "max" to 99
                    )

                    val limits = hashMapOf(
                        "applicative" to limit,
                        "compilation" to limit,
                        "corporate" to limit,
                        "erasmus" to limit,
                        "research" to limit,

                    )

                    Firebase.firestore.collection("account").document(id).collection("sessions").document("december")
                        .set(limits)
                    Firebase.firestore.collection("account").document(id).collection("sessions").document("july")
                        .set(limits)
                    Firebase.firestore.collection("account").document(id).collection("sessions").document("march")
                        .set(limits)
                    Firebase.firestore.collection("account").document(id).collection("sessions").document("september")
                        .set(limits)
                }
            }
        }
    }

    fun returnThesis(id: String){
        getThesis(id)
    }

    private fun getThesis(id:String){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(id).collection("thesis").addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e)
                    return@addSnapshotListener
                }
                if( value != null ) {
                    val thesis = ArrayList<Thesis>()
                    val documents = value.documents

                    documents.forEach {
                        val temp = it.toObject(Thesis::class.java)
                        if(temp != null){
                            temp.id = it.id
                            //temp.id_professor = _userData.value.id
                            thesis.add(temp)
                        }

                    }
                    _thesis.value = thesis
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
                                2 -> "research"
                                3 -> "corporate"
                                else -> "erasmus"
                            }

                            Firebase.firestore.collection("account").document(_userData.value.id).collection("sessions").document(temp)
                                .get().addOnSuccessListener { document ->

                                    val sessionReached = document.toObject<Session>()!!
                                    val temp3 = when(temp2){
                                        "applicative" -> sessionReached.applicative.values
                                        "compilation" -> sessionReached.compilation.values
                                        "corporate" -> sessionReached.corporate.values
                                        "erasmus" -> sessionReached.erasmus.values
                                        else ->  sessionReached.research.values
                                    }

                                    if(_userData.value.hasLimit && temp3.toList()[0] >= temp3.toList()[1]){
                                        val text = "Limite raggiunto o superato!"
                                        val duration = Toast.LENGTH_SHORT
                                        val toast = Toast.makeText(context,text, duration)
                                        toast.show()
                                    } else {
                                        Firebase.firestore.collection("account").document(_userData.value.id).collection("sessions").document(temp)
                                            .update(
                                                "${temp2}.current",FieldValue.increment(1)
                                            )

                                        Firebase.firestore.collection("requests").document(id)
                                            .update(mapOf("accepted" to true))

                                        Firebase.firestore.collection("account").document(idStudent)
                                            .update(mapOf("hasThesis" to true))

                                        Firebase.firestore.collection("account").document(idStudent).collection("thesis").addSnapshotListener { thesis, e ->

                                            if (e != null) {
                                                Log.w("TAG", "Listen failed.", e)
                                                return@addSnapshotListener
                                            }

                                            if(thesis != null){
                                                    for(t in thesis){
                                                        if(t.id != idThesis){
                                                            Firebase.firestore.collection("account").document(idStudent).collection("thesis")
                                                                .document(t.id).delete()
                                                        }
                                                    }
                                                }
                                            }

                                        Firebase.firestore.collection("requests").whereEqualTo("id_student",idStudent).whereEqualTo("accepted",false).addSnapshotListener { request, e ->
                                                if (e != null) {
                                                    Log.w("TAG", "Listen failed.", e)
                                                    return@addSnapshotListener
                                                }

                                                if(request != null){
                                                    for(r in request){
                                                        Firebase.firestore.collection("requests").document(r.id).delete()
                                                    }
                                                }
                                            }
                                    }
                                }

                        } else {
                            Firebase.firestore.collection("requests").document(id)
                                .delete()
                            if(thesis!!.type == 3 || thesis.type == 4){
                                Firebase.firestore.collection("account").document(idStudent).collection("thesis").document(idThesis)
                                    .delete()
                            } else {
                                sendThesis(idStudent,idThesis,thesis)
                            }

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
        //getImage(_userData.value)
        _user?.let {
            _userImage.value = _user?.photoUrl
        }

    }

    fun account(id: String){
        getAccount(id)
    }
    fun accountProfessor(account: Account){
        _visitedAccount.value = account
    }


    private fun getAccount(id: String){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(id).addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if(value != null){
                    val temp = value.toObject<Account>()
                    if(temp != null){
                        temp.id = value.id
                        temp.isProfessor = false
                        _visitedAccount.value = temp
                    }
                }
            }
        }
    }

    fun getAccountsByType(type: Boolean){
        getAccounts(type)
    }

    private fun getAccounts(type: Boolean) {
        viewModelScope.launch {
            if(type){
                Firebase.firestore.collection("account").whereEqualTo("isProfessor",type)
                    .addSnapshotListener { value, e ->
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e)
                            return@addSnapshotListener
                        }

                        if(value != null){
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
                                        image = it.image,
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
        viewModelScope.launch {
            storage.child("images/${profile.id}").downloadUrl.addOnSuccessListener {
                _userImage.value = it
            }
        }
    }
    fun retrieveImageRequest(profile: Request, onAssign:(Uri) -> Unit){
        viewModelScope.launch {
            val temp = if(userData.value.isProfessor){
                profile.id_student
            } else {
                profile.id_professor
            }
            storage.child("images/${temp}").downloadUrl.addOnSuccessListener {
                onAssign(it)
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
        user?.updateEmail(email)
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
                    if (e != null) {
                        Log.w("TAG", "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    if(value != null) {

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

    fun addNewCustomRequest(title: String, supervisor: String, type: String, session: Int,professorId: String,context: Context?){
        addNewThesis(title,type,supervisor,professorId)

        Firebase.firestore.collection("account").document(_userData.value.id).collection("thesis").addSnapshotListener { value, e ->
            if (e != null) {
                Log.w("TAG", "Listen failed.", e)
                return@addSnapshotListener
            }
            if( value != null ) {

                val typeTemp = when(type){
                    "Tesi in azienda" -> 3
                    else -> 4
                }
                val documents = value.documents
                documents.forEach {
                    val temp = it.toObject(Thesis::class.java)
                    if(temp != null && temp.title == title && temp.description == supervisor){
                        temp.id = it.id
                        temp.id_professor = _userData.value.id
                        addNewRequest(userData.value.id, _visitedAccount.value.id,temp.id,userData.value.name, session, typeTemp, title,userData.value.email, context)
                    }
                }
            }
        }
    }

    fun addNewRequest(idStudent: String, idProfessor: String, id_thesis: String, name: String, session: Int,type: Int, thesisTitle: String,email:String,context: Context?){
        val newRequest = getNewRequest(idStudent,idProfessor,id_thesis,name,session,type,email,thesisTitle)
        val temp = when(session){
            0 -> "march"
            1 -> "july"
            2 -> "september"
            else -> "december"
        }

        Firebase.firestore.collection("account").document(idProfessor).collection("sessions").document(temp).get().addOnSuccessListener {
           val checkSession = it.toObject<Session>()!!
            val temp2 = when(type){
                0 -> checkSession.applicative
                1 -> checkSession.compilation
                2 -> checkSession.research
                3 -> checkSession.corporate
                else -> checkSession.erasmus
            }

            if(temp2.values.toList()[0] >= temp2.values.toList()[0]){
                val text = "Il professore non può accettare tesi per la sessione e tipologia selezionata"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context,text, duration)
                toast.show()
            } else {
                insertRequest(newRequest)
                if( id_thesis != ""){
                    Firebase.firestore.collection("account").document(idProfessor).collection("thesis").document(id_thesis).get()
                        .addOnSuccessListener { th ->
                            if(th != null){
                                val thesis = th.toObject(Thesis::class.java)
                                if (thesis != null) {
                                    sendThesis(idProfessor, id_thesis,thesis)
                                }
                            }
                        }
                }
            }

        }


    }

    private fun getNewRequest(idStudent: String, idProfessor: String, idThesis: String,name: String, session: Int, type: Int, email:String,thesisTitle: String): Request{
        return Request(
            id_student = idStudent,
            id_professor = idProfessor,
            id_thesis = idThesis,
            name = name,
            session = session,
            type = type,
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

    fun addNewThesis(thesisName: String, thesisType: String,thesisDescription: String,idProfessor: String) {
        val temp = when(thesisType){
            "Tesi Compilativa" -> 0
            "Tesi Applicativa" -> 1
            "Tesi di ricerca" -> 2
            "Tesi in azienda" -> 3
            else -> 4

        }
        val newThesis = getNewThesisEntry(thesisName, temp, thesisDescription, idProfessor)
        insertThesis(newThesis)
        onDialogConfirm()
    }

    private fun getNewThesisEntry(thesisName: String, thesisType: Int, thesisDescription: String,idProfessor: String): Thesis {
        return Thesis(
            title = thesisName,
            type = thesisType,
            description = thesisDescription,
            id_professor = idProfessor
        )
    }

    private fun insertThesis(thesis: Thesis) {
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(_userData.value.id).collection("thesis").add(thesis)
        }
    }

    //Add Thesis Dialog
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _showFloatingButton = MutableStateFlow(false)
    val showFloatingButton: StateFlow<Boolean> = _showFloatingButton.asStateFlow()

    private val _showTabRow = MutableStateFlow(false)
    val showTabRow: StateFlow<Boolean> = _showTabRow.asStateFlow()

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

    fun showFloating(show: Boolean){
        _showFloatingButton.value = show
    }

    fun showTab(show: Boolean){
        _showTabRow.value = show
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
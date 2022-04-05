# REST API CON SPRING BOOT
## Introduzione

Il codice sorgente implementa una semplice API per recuperare dati relativi a studenti (nome e indirizzo e-mail). L'obiettivo è quello di utilizzare le modalità
di sviluppo standard del framework *Spring Boot* per la creazione di applicazioni back-end per un progetto base, implementando le 5 chiamate fondamentali di qualsiasi
REST API (GET (totale e di un singolo elemento), POST, DELETE, PUT), la gestione lato server degli errori 4xx e il testing delle varie funzionalità tramite *integration test*, utilizzando le librerie *JUnit* e *Mockito*.  

## Struttura del applicazione
### Model
La classe *Student* implementa una semplice struttura per tutte le informazioni necessarie mantenute all'interno del database:
* Nome completo (name)
* E-Mail (mail)
* Numero identificativo (id)

La classe segue due annotazioni, ossia *@Entity*, che sottolinea il fatto che la classe rappresenti un'entità dl modello relazionale, e *@Table*, che invece indica che
la rappresentazione sottostante è quella seguita dallo schema principale del DB.
L'attributo *id* utilizza due annotazioni, ossia *@Id*, che indica l'attributo utilizzato come chiave primaria della relazione, e *@GeneratedValue*, che indica che quel
campo non dovrà essere inserito all'interno delle richieste HTTP, poichè il suo valore verrà calcolato automaticamente.
Inoltre, tutti gli attributi sono annotati con *@Getter* e *@Setter*, annotazioni della *dependency **Lombok***, in modo tale che i metodi *get* e *set* vengano
implementati automaticamente.

### Controller

La classe *StudentController* permette di gestire le risposte che il server invia al client, grazie all'introduzione dell'annotazione ***@RestController***; l'altra
annotazione usata a livello di classe è *@RequestMapping("api/v1/students")* che indica l'URL su cui vengono mappate le richieste API. Ogni metodo è inoltre annotato
in base alla richiesta HTTP che soddisfa (***@[RequestName]Mapping***, dove ***[RequestName]*** sarà Get, Post, ...). Dove necessario, viene indicato anche il completamento dell'URL (tra parentesi, spesso - quasi sempre - ("/{id}"), indicando dunque un valore variabile
pari all'URI della risorsa richiesta), che viene recuperato dal metodo utilizzando l'annotazione *@PathVariable* sull'argomento specifico.
Per quei metodi in cui il client passa informazioni al server tramite il corpo della richiesta, i metodi che gestiscono tali chiamate recuperano tali informazioni usando
l'annotazione *@RequestBody* prima del argomento del metodo che rappresenta l'entità.
I cinque servizi implementati sono quelli fondamentali delle REST API:
* getStudents, chiamata HTTP GET che restituisce l'intera lista di studenti mantenuti nel database
* getStudent(id), chiamata HTTP GET che restituisce lo studente con l'id indicato all'interno della richiesta
* addStudent, chiamata POST HTTP che aggiunge un nuovo studente al database
* deleteStudent(id), chiamata DELETE HTTP che elimina lo studente con id indicato nella richiesta
* updateStudent(id, student), chiamata PUT HTTP che aggiorna lo studente con id indicato con il nuovo studente, passato tramite il corpo della funzione
  Le varie risposte HTTP sono contrassegnate da uno stato, che se non viene indicato è 200 in caso di successo (HTTP STATUS OK). Nel caso in cui viene indicato un codice
  diverso, come nella chiamata POST, allora si usa l'annotazione *@ResponseStatus(HTTP_STATUS)*, dove HTTP_STATUS indica lo stato che la richiesta genera (nell'esempio,
  *CREATED*).
  Il controller utilizza un *Service* (segue), che viene inizializzato e implementato automaticamente grazie all'anotazione *@Autowired*

### Service

La classe *StudentService* implementa quella che viene indicata come *business logic*, ossia tutta quella parte di codice di controllo relativo alle informazioni
ricevute dal client, mantenendo più pulito il codice del controller.
Questa classe, dunque, implementa un metodo per ogni chiamata del *RestController*, effettuando i controlli del caso (dove neccessario):
* nel caso in cui i controlli vengono passati, vengono recuperati i dati dal database e restituiti al controller, che li invierà al client come risposta HTTP;
* nel caso in cui i controlli non vengono superati, viene lanciato un errore, che varia in base al tipo di problema risontrato; nel caso dell'applicazione di esempio,
  i controlli sono effettuati sull'esistenza di un elemento con id specificato dalla richiesta client, la validità del formato dell'indirizzo mail usato e le mail duplicate.

La classe viene annotata come @Service, in modo tale che la sua natura sia riconoscibile (inoltre, ci permette di implementare *dependency injection*) e utilizza, per recuperare i dati, una *studentRepo* (segue), inizializzta e
implementata usando l'annotazione @Autowired

### Repository
L'interfaccia *StudentRepo* che estende l'interfaccia *JpaRepository*, si interfaccia al database
MySQL reale, più precisamente alla tabella *student*, tramite le informazioni inserite all'interno del file *application.properties* (nome del DB, password, user, URL del server host del DB, nome del DB).
*JPA* fornisce i metodi base per reperire/modificare le istanze del database (ad esempio, *save()* per aggiungere, *delete()* per eliminare, ricerca per ID con
*findById(id)*...), ma è possibile aggiungere nuovi metodi, in base alle esigenze dell'applicazione. Per fare questo, basta indicare al metodo la query da
eseguire sulla tabella con l'ausilio dell'annotazione *@Query(...)*, indicando quindi tra parentesi l'interrogazione necessaria che verrà eseguita nel
momento in cui il metodo verrà chiamato.
Questa interfaccia è molto semplice, ma altrettanto importante per completare tutti i layer dell'applicazione.

## Gestione degli errori

Nel caso in cui vengano riscontrati degli errori, è necessario gestirli per restituire al client informazioni utili. Nel caso di questa semplice applicazione,
abbiamo inserito 3 errori da gestire:
* *StudentNotFoundException*, che restituisce uno stato NOT FOUND (404)
* *MailTakenExcpetion*, che restituisce uno stato BAD REQUEST (400)
* *MailFormatExcpetion*, che restituisce uno stato BAD REQUEST (400)

Lo stato della risposta HTTP viene modificato utilizzando l'annotazione *@ResponseStatus(HTTP_STATUS)* a livello di classe.
Gli errori lanciati vengono poi gestiti dall'applicazione tramite la classe *StudentErrorHanlder*, con annotazione *@ControllerAdvice*, che dispone di
tre metodi, uno per ogni errore da gestire; ogni metodo ha 3 anntoazioni:
* *@ResponseBody*;
* *@ExceptionHandler(Exception.class)*, dove tra parentesi viene inserita la classe dell'errore che il metodo gestisce;
* *@ResponseStatus(HTTP_STATUS)*, dove viene indicato lo stato della risposta dell'errore;

I tre metodi sono molto semplici, perchè non fanno altro che restituire il messaggio dell'errore lanciato, che viene inserito nel corpo della risposta HTTP
(l'annotazione *@ControllerAdvice* serve proprio a questo).

## Test

L'applicazione presenta anche una parte di *integration test*, che ha il compito di testare il comportamento del controller per le varie chiamate API.
La classe *StudentControllerTest* utilizza l'annotazione *@WebMvcTest*, che permette di implementare dei test sull'intera infrastruttura. La classe utilizza
uno *StudentService* *mock* (finto), un *web application context* e un *MockMvc*: questi componenti permettono di implementare una struttura di test che ci
permetta di capire il comportamento dell'applicazione per ogni richiesta HTTP senza effettuare alcuna chiamata reale.
Un'altra importante libreria che viene utilizzata per il testing, oltre a JUnit 5, è *Mockito*, che ci permette, tra le sue funzionalità, di indicare il valore
restituito da un metodo nel momento in cui viene chiamato.
### Esempio: shouldReturnStudent()
Questo metodo ha il compito di testare una chiamata (corretta) per recuperare uno studente dal database (test del metodo *getStudent(id)*).
1. Nella prima parte del metodo, viene indicato il comportamento che deve avere il test nel momento in cui viene chiamato il metodo *service.getStudent*;
   *when(...).thenReturn(...)* fa esattamente questa cosa: **quando** viene richiamato il metodo *getStudent(1)*, **allora restituisci** *new Student("Mario Rossi", "mario.rossi@student.com")*.
2. Nella seconda fase, viene effettuata la chiamata di test, tramite il metodo **perform**; i vari metodi **andExpect** permettono di indicare i diversi valori attesi come risposta, come se fosse una *assertion*; è possibile effettuare i test sulla risposta HTTP in base a diversi
   parametri, dal contenuto dell'header, allo stato della risposta fino al contenuto del corpo della risposta. Nell'esempio, viene verificato lo stato della risposta
   e i valori attesi degli attributi *name* e *mail* ricevuti come risposta.

Oltre ad implementare i test per ogni chiamata API corretta, sono stati implementati anche test per verificare il comportamennto dell'applicazione in caso di
errore (NotFound, MailFormat, MailTaken), per capire se i codici di errore sono stati implementati correttamente. In questo caso, la chiamata *when()* è
seguita da una chiamata *thenThrow()*, che permette di lanciare un errore.
La parte di testing è importante, perchè permette di correggere errori nel codice prima di mettere in funzione effettivamente l'applicazione.

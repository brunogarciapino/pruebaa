package gui
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AwtWindow
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.FileDialog
import java.awt.Frame
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import java.io.File

internal fun AppGui() = application {
    val titleWindowIni = "Procesa archivo: "
    var isFileChooserOpen by remember { mutableStateOf(false) } //Indica que se quiere abrir el FileChooser
    var titleWindow by remember { mutableStateOf(titleWindowIni) } //Titulo de la ventana
    var filePath by remember { mutableStateOf("") } //Path al fichero que se está procesando
    var isActiveProcess by remember { mutableStateOf(false) } //Activar o desactivar el menuItem  procesar
    var isActiveExport by remember { mutableStateOf(false) } //Activar o desactivar el menuItem  procesar
    var textProcesed by remember { mutableStateOf("") } //El texto a poner en el campo de procesado.
    var salida = ""


    //Ventana principal.
    Window(
        title = titleWindow,
        onCloseRequest = ::exitApplication
    ) {
        //MenuBar de la ventana
        MenuBar {
            Menu("Archivo") {
                Item("Abrir", onClick = { isFileChooserOpen = true })
                Item("Procesar", onClick = {
                    textProcesed = procesar(textProcesed)
                    isActiveExport = true
                    salida = mover(filePath)
                },
                    enabled = isActiveProcess)
                Item("Exportar", onClick ={
                    File(salida).writeText(textProcesed)
                },
                    enabled = isActiveExport)
                Item("Salir", onClick = ::exitApplication)
            }

        }
        //El resto de la ventana
        FrameWindow(
            isFileChooserOpen,
            filePath = filePath,
            onCloseFileChooser = { directory: String?, fileName: String? -> //Cuando se elige en archivo.
                isFileChooserOpen = false
                filePath= if(fileName.isNullOrBlank()) filePath else directory+fileName
                isActiveProcess = filePath.isNotBlank()
                titleWindow = if (filePath.isBlank()) titleWindowIni else titleWindowIni + fileName
                textProcesed = File(filePath).readText(Charsets.UTF_8)
            },
            onClickSelectFile = { isFileChooserOpen = true },
            textProcesed
        )
    }
}

private fun procesar(textProcesed: String): String { //Funcion donde se prepara los comandos
    var textProcesed1 = textProcesed
    var lineas: List<String> = mutableListOf()
    var fotos: List<String> = mutableListOf()
    var res: List<String> = mutableListOf()
    var indices = textProcesed1.split("[\r\n]+".toRegex()).toTypedArray() //Se elimina tantos los saltos de linea como los espacios
    var line = indices.size
    for (i in 0..line-1) {
        lineas += textProcesed1.split("\n")[i]
    }
    for(i in 1..lineas.size-1) {
        lineas[i].split(" ").forEach { fotos += it } //Se elimina los espacios de las lineas que tienes fotos con espacios
    }
    var ciudad = lineas[0]
    for (i in 0..fotos.size-1){
        var fotocity = ciudad + "00$i.jpg" //Se crea el comando de foto

    }
    val enter = "\n" //Hace que por cada comando se añada un salto de linea
    val final = res.joinToString(enter)
    return final
}

private fun mover(filePath: String ): String{
    val modifier = filePath.dropLast(2) //Elimina las dos ultimas letras que hay
    val arch = "ans"
    val salida = modifier + arch
    return salida
}

/**
 * TODO
 *
 * @param isFileChooserOpen Si se ha elegido abrir el FileChooser
 * @param filePath El path al archivo
 * @param onCloseFileChooser Evento que se lanza cuando se cierra el FileChooser. Devuelve el path y nombre del archivo seleccionado. Null si no se cancelo.
 * @param onClickSelectFile Evento al pulsar sobre el botón Abrir
 * @param textProcesed El texto que contiene los resultados, se vuelcan en el textField que contiene los resultados
 */
@Composable
internal fun FrameWindow(
    isFileChooserOpen: Boolean = false,
    filePath: String = "",
    onCloseFileChooser: (directory: String?, file: String?) -> Unit,
    onClickSelectFile: () -> Unit,
    textProcesed: String
) {
    MaterialTheme {
        if (isFileChooserOpen)
            FileChooser(onCloseFileChooser = onCloseFileChooser)

        Box(
            modifier = Modifier.fillMaxSize()
                .background(color = Color(180, 180, 180))
                .padding(10.dp)
        )
        {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 12.dp, bottom = 12.dp)

            ) {
                Column {

                    TextField(
                        // Contiene el path+nombre del fichero leido
                        value = filePath,
                        onValueChange = { },
                        label = { Text("Seleccione un archivo:") },
                        placeholder = { Text("Archivo no seleccionado") },
                        modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                            .fillMaxWidth(),
                        leadingIcon = { //Icono que funciona igual que la opción del menuItem Abrir
                            IconButton(onClick = onClickSelectFile) {
                                Icon(
                                    imageVector = Icons.Filled.Create,
                                    contentDescription = "Seleccione un archivo"
                                )
                            }
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color(0xFF120524),
                            backgroundColor = Color.White,
                            focusedLabelColor = Color(0xFF120524).copy(alpha = ContentAlpha.high),
                            focusedIndicatorColor = Color.Transparent,
                            cursorColor = Color(0xFF120524),
                        ),
                        readOnly = false,
                    )
                    Space()
                    TextField(
                        // Campo de texto que contiene el texto en el que se vuelcan los resultados
                        value = textProcesed,
                        onValueChange = { },
                        label = { Text("Resultado") },
                        placeholder = { Text("No obtenido resultados") },
                        modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                            .fillMaxSize().width(300.dp)
                            .height(200.dp),
                        readOnly = false,

                        )
                }

            }
        }
    }
}
/**
 * TODO
 *
 * @param parent
 * @param onCloseFileChooser Evento que se lanza cuando se cierra FileChooser. Devuelve directory en el que se encuentra el fichero y file el nombre del fichero.
 */

@Composable
fun FileChooser(
    parent: Frame? = null,
    onCloseFileChooser: (directory: String?, file: String?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(parent, "Seleccione un archivo", LOAD) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    onCloseFileChooser(directory, file)
                }
            }
        }
    },
    dispose = FileDialog::dispose

)


@Composable
internal fun Space() {
    Spacer(modifier = Modifier.size(16.dp))
}


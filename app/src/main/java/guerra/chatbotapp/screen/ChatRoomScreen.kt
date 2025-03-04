package guerra.chatbotapp.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import guerra.chatbotapp.R
import guerra.chatbotapp.data.Room
import guerra.chatbotapp.viewmodel.RoomViewModel

@Composable
fun ChatRoomScreen(
    roomViewModel: RoomViewModel,
    context: Context
) {
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }

    val rooms by roomViewModel.rooms.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Chat Rooms", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Icon(
                modifier = Modifier.clickable { Toast.makeText(context, "Account Settings TODO", Toast.LENGTH_SHORT).show() },
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Account"
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(rooms) {
                RoomItem(room = it, context = context)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Room")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest =
            {
                showDialog = false
                name = ""
            },
            title = { Text(text = "Create a new room") },
            text = {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }, confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                roomViewModel.createRoom(name.trim()) {
                                    Toast.makeText(
                                        context,
                                        "Room Created Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    showDialog = false
                                    roomViewModel.loadRooms()
                                }
                            }
                        }
                    ) {
                        Text("Add")
                    }
                    Button(
                        onClick = { showDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            })
    }
}

@Composable
fun RoomItem(
    room: Room,
    context: Context
) {
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                1.dp,
                Color(ContextCompat.getColor(context, R.color.gray)),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = room.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            OutlinedButton(
                onClick = { isLoading = !isLoading },
                modifier = Modifier
                    .height(40.dp)
                    .width(100.dp)
            ) {
                if (!isLoading) {
                    Text("Join")
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color(ContextCompat.getColor(context, R.color.purple_500)),
                        strokeWidth = 1.dp
                    )
                }
            }
        }
    }
}

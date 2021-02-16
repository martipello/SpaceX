import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sealstudios.spacex.databinding.LaunchViewHolderLoadingStateLayoutBinding

class LaunchesLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LaunchesLoadStateAdapter.LaunchViewHolderLoadingState>() {

    override fun onBindViewHolder(holder: LaunchViewHolderLoadingState, loadState: LoadState) =
        holder.bind(loadState)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LaunchViewHolderLoadingState {
        return LaunchViewHolderLoadingState(
            LaunchViewHolderLoadingStateLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    inner class LaunchViewHolderLoadingState(private val launchViewHolderLoadingStateLayoutBinding: LaunchViewHolderLoadingStateLayoutBinding) :
        RecyclerView.ViewHolder(launchViewHolderLoadingStateLayoutBinding.root) {
        fun bind(loadState: LoadState) {
            with(launchViewHolderLoadingStateLayoutBinding) {

                loading.isVisible = loadState is LoadState.Loading
                launchViewHolderErrorLayout.root.isVisible = loadState is LoadState.Error
                if (loadState is LoadState.Error) {
                    launchViewHolderErrorLayout.errorText.text = loadState.error.localizedMessage
                }
                launchViewHolderErrorLayout.retryButton.setOnClickListener {
                    retry.invoke()
                }
            }
        }
    }
}